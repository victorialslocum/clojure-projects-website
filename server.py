# to run this (lol)
# export FLASK_APP=server.py
# export FLASK_ENV=development
# flask run

# todo: see if we can make these into one line of code
from flask import Flask
from flask import render_template
import os


app = Flask(__name__)

@app.route('/')
def hello_world():

    codeStuff = []

    for tempSubFolder in os.listdir('code/'):
        if os.path.isdir(os.path.join('code/', tempSubFolder)):
            # tempSubFolder is the directory name
            # find the files for the tempSubFolder and load into tempfiles
            tempFiles = []
            for file in os.listdir("code/" + tempSubFolder + "/"):
                tempFiles.append(file)
            
            # add a dictionary of the folder name and the files into codeStuff
            codeStuff.append({"dirName": tempSubFolder, "files": tempFiles}) #Victoria question: Why does it not work when you take out the "" parts?

    return render_template('render-files.html', files=codeStuff)
