import os
import shutil
from flask import Flask, request
from modules.inference import predict_class
from datetime import datetime


app = Flask(__name__)
pred = predict_class(home_path="./modules")

@app.route('/', methods=['GET', 'POST'])
def postHandler():
    imagefile = request.files['image']
    filename = imagefile.filename
    print("\nReceived image File name : " + imagefile.filename)
    imagefile.save(filename)
    now = datetime.now()
    current_datetime_str = now.strftime("%Y%m%d%H%M%S")
    shutil.copy(filename, os.path.join("archive",current_datetime_str)+".jpg")
    return pred.predict(filename)

app.run(host='0.0.0.0', port=8001, debug=False)
