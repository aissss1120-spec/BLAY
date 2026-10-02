import os
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(title="BLAY Online Server", version="1.8.0")

MODELS = {
    "qwen": os.getenv("QWEN_ENDPOINT", ""),
    "sahabatai": os.getenv("SAHABATAI_ENDPOINT", ""),
    "phi": os.getenv("PHI_ENDPOINT", ""),
}

class Command(BaseModel):
    text: str
    device_id: str = "unknown"

@app.get("/health")
def health():
    return {"status": "ok", "version": "1.8.0"}

@app.get("/v1/models/status")
def model_status():
    return {
        "router": "online",
        "models": {
            name: {"configured": bool(url), "endpoint": url}
            for name, url in MODELS.items()
        }
    }

@app.post("/v1/command")
def command(req: Command):
    # Prototype router. Real model inference requires configured online endpoints.
    text = req.text.strip()
    if not text:
        return {"ok": False, "error": "empty_command"}

    return {
        "ok": True,
        "router": "qwen" if MODELS["qwen"] else "fallback",
        "answer": f"BLAY menerima: {text}",
        "action": None,
    }
