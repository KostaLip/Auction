from flask import Flask, request, jsonify
import base64
import json
import os

app = Flask(__name__)

POD_OBJECTS_HANDLERS = {
    'Pod': lambda object: object.get('spec'),
    'Deployment': lambda object: object.get('spec', {}).get('template', {}).get('spec'),
    'StatefulSet': lambda object: object.get('spec', {}).get('template', {}).get('spec'),
    'DaemonSet': lambda obj: obj.get('spec', {}).get('template', {}).get('spec'),
    'ReplicaSet': lambda obj: obj.get('spec', {}).get('template', {}).get('spec'),
    'Job': lambda obj: obj.get('spec', {}).get('template', {}).get('spec'),
    'CronJob': lambda obj: obj.get('spec', {}).get('jobTemplate', {}).get('spec', {}).get('template', {}).get('spec')
}

def get_pod_spec(obj, kind):
    handler = POD_OBJECTS_HANDLERS.get(kind)
    if handler:
        return handler(obj)
    return None

def mutate_pod_spec(pod_spec):
    if not pod_spec:
        return
    
    if 'securityContext' not in pod_spec:
        pod_spec['securityContext'] = {}
    
    pod_spec['securityContext']['runAsUser'] = 7777
    pod_spec['securityContext']['runAsNonRoot'] = True
    
    for container in pod_spec.get('containers', []):
        if 'securityContext' not in container:
            container['securityContext'] = {}
        container['securityContext']['runAsNonRoot'] = True
        container['securityContext']['runAsUser'] = 7777

        if 'capabilities' not in container['securityContext']:
            container['securityContext']['capabilities'] = {}
        container['securityContext']['capabilities']['drop'] = ['ALL']

    for container in pod_spec.get('initContainers', []):
        if 'securityContext' not in container:
            container['securityContext'] = {}
        container['securityContext']['runAsNonRoot'] = True
        container['securityContext']['runAsUser'] = 7777

        if 'capabilities' not in container['securityContext']:
            container['securityContext']['capabilities'] = {}
        container['securityContext']['capabilities']['drop'] = ['ALL']

    for container in pod_spec.get('ephemeralContainers', []):
        if 'securityContext' not in container:
            container['securityContext'] = {}
        container['securityContext']['runAsNonRoot'] = True
        container['securityContext']['runAsUser'] = 7777

        if 'capabilities' not in container['securityContext']:
            container['securityContext']['capabilities'] = {}
        container['securityContext']['capabilities']['drop'] = ['ALL']

@app.route('/mutate', methods=['POST'])
def mutate():
    admission_review = request.get_json()

    req = admission_review['request']
    uid = req['uid']
    kind = req['kind']['kind']
    obj = req['object']

    pod_spec = get_pod_spec(obj, kind)

    if pod_spec is None:
        return jsonify({
            "apiVersion": "admission.k8s.io/v1",
            "kind": "AdmissionReview",
            "response": {
                "uid": uid,
                "allowed": True
            }
        })
    
    mutate_pod_spec(pod_spec)

    if kind == 'Pod':
        patch_path = "/spec"
    elif kind == 'CronJob':
        patch_path = "/spec/jobTemplate/spec/template/spec"
    else:
        patch_path = "/spec/template/spec"

    patch = json.dumps([
        {
            "op": "replace",
            "path": patch_path,
            "value": pod_spec
        }
    ])

    return jsonify({
        "apiVersion": "admission.k8s.io/v1",
        "kind": "AdmissionReview",
        "response": {
            "uid": uid,
            "allowed": True,
            "patchType": "JSONPatch",
            "patch": base64.b64encode(patch.encode()).decode()
        }
    })

@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "healthy"}), 200

if __name__ == '__main__':
    cert_path = os.getenv('TLS_CERT_PATH', '/certs/tls.crt')
    key_path = os.getenv('TLS_KEY_PATH', '/certs/tls.key')

    if os.path.exists(cert_path) and os.path.exists(key_path):
        app.run(host='0.0.0.0', port=5000, 
                ssl_context=(cert_path, key_path))
    else:
        app.run(host='0.0.0.0', port=5000, debug=True)