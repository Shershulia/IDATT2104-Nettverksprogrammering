# base image
FROM python:3

# set working directory
WORKDIR /src

# copy the requirements file into the container
COPY requirements.txt .

# install dependencies
RUN pip install -r requirements.txt

# copy the content of the local src directory to the working directory
COPY . .

# expose port
EXPOSE 5000

# start server
CMD [ "python", "-u", "main.py" ]