from flask import Flask, g

app = Flask(__name__)
g.setdefault("as", [1, 2, 3])

@app.route('/')
def hello_world():
    return g.get("as")


if __name__ == '__main__':
    app.run()
