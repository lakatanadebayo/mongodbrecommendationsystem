# sbert_api.py
from fastapi import FastAPI
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer

app = FastAPI()
model = SentenceTransformer('all-MiniLM-L6-v2')  # ou un autre modèle SBERT

class TextRequest(BaseModel):
    text: str

@app.post("/encode")
def encode(request: TextRequest):
    embedding = model.encode(request.text).tolist()
    return {"embedding": embedding}