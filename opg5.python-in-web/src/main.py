from flask import Flask, request, jsonify
from flask_cors import CORS
import sys
from io import StringIO


app = Flask(__name__)
CORS(app)


@app.route('/', methods=['POST'])
def execute_code():

    code = request.json.get('code')
    try:
        old_stdout = sys.stdout
        redirected_output = sys.stdout = StringIO()
        exec(code)
        sys.stdout = old_stdout

        return jsonify({'result': redirected_output.getvalue()})
    except Exception as e:
        return jsonify({'result': str(e)}), 400

@app.route('/', methods=['GET'])
def get():
    return jsonify({'result': "hello"})


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
