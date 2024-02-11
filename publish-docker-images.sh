#! /bin/bash -e

export CR_PAT="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"
echo $CR_PAT | docker login ghcr.io -u USERNAME --password-stdin

#cr-voucher-service cr-order-service cr-account-service cr-notification-service
IMAGES="cr-order-service cr-api-gateway cr-voucher-history-service cr-user-service"
for image in $IMAGES; do
  cd "${image}"
  docker tag "code-redemption-$(echo "$image" | sed 's/^cr-//')" "ghcr.io/azampay-ltd/code-redemption/${image}:1.0"
  docker push "ghcr.io/azampay-ltd/code-redemption/${image}:1.0"
  cd ../
done