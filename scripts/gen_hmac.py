import hmac
import hashlib
import base64

# ==== CONFIGURE THESE ====
secret = "super_secret_key_123"        # Must match your backend
idempotency_key = "op-collect-20240603-testA"       # Change per request/test
body = '{"senderUpi":"oliver@upi","receiverUpi":"lucas@upi","amount":3490.40}'  # Exactly as in curl -d

# ==== MUST CONCATENATE LIKE BACKEND ====
message = body + idempotency_key

# ==== HMAC CALCULATION ====
h = hmac.new(secret.encode(), message.encode(), hashlib.sha256)
hmac_b64 = base64.b64encode(h.digest()).decode()

print("X-HMAC:", hmac_b64)
