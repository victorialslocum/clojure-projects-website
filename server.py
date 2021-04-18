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


    # 1. go through subfolders
    # ......
    # FOUND ONE! ALGORITHMS
    # ok so whats in algoritms
    # caskdjl.clj, sadsad.clj
    # let's add those to a list for the "algorithms" folder!
    # FOUND ONE! Language
    # ok so whats in algoritms
    # caskdj2l.clj, sa2dsad.clj
    # let's add those to a list for the "language" folder!

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

# directory1
#   file1
#   file 2
# directory2