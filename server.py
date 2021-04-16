# to run this (lol)
# export FLASK_APP=hello.py
# export FLASK_ENV=development
# flask run

# todo: see if we can make these into one line of code
from flask import Flask
from flask import render_template
import os


app = Flask(__name__)

victoriavar = "injected into code 🙂"

@app.route('/')
def hello_world():

    codeStuff = []

    for subFolder in os.listdir('code/'):
        if os.path.isdir(os.path.join('code/', subFolder)):
            # subFolder is the directory name
            # find the files for the subfolder and load into tempfiles
            tempFiles = []
            for file in os.listdir("code/" + subFolder + "/"):
                tempFiles.append(file)
            
            # add a dictionary of the folder name and the files into codeStuff
            codeStuff.append({"dirName": subFolder, "files": tempFiles})
            
    return render_template('render-files.html', files=codeStuff)

# directory1
#   file1
#   file 2
# directory2