import hmac
import hashlib
import base64

# ==== CONFIGURE THESE ====
secret = "super_secret_key_123"        # Must match your backend
idempotency_key = "op-test-20240603-xyz"       # Change per request/test
body = '{"senderUpi":"toby@upi","receiverUpi":"grace@upi","amount":7182.50}'  # Exactly as in curl -d

# ==== MUST CONCATENATE LIKE BACKEND ====
message = body + idempotency_key

# ==== HMAC CALCULATION ====
h = hmac.new(secret.encode(), message.encode(), hashlib.sha256)
hmac_b64 = base64.b64encode(h.digest()).decode()

print("X-HMAC:", hmac_b64)
