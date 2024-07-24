FROM python:3.9-slim
RUN pip3 install --upgrade pip
RUN pip3 install torch torchvision --index-url https://download.pytorch.org/whl/cpu
RUN pip3 install flask
RUN mkdir /app
COPY . /app
WORKDIR /app/server_and_model
ENTRYPOINT ["python","server.py"]

