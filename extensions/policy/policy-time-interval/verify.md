

```bash

# asset
curl -H 'Content-Type: application/json' -d @extensions/policy/policy-time-interval/1-asset.json -X POST "http://localhost:18181/management/v3/assets" -s | jq

# policy
curl -H 'Content-Type: application/json' -d @extensions/policy/policy-time-interval/2-policy-definition.json -X POST "http://localhost:18181/management/v2/policydefinitions" -s | jq

# contract
curl -H 'Content-Type: application/json' -d @extensions/policy/policy-time-interval/3-contract-definition.json -X POST "http://localhost:18181/management/v2/contractdefinitions" -s | jq




# get dataset
curl -H 'Content-Type: application/json' -d @extensions/policy/policy-time-interval/4-get-dataset.json -X POST "http://localhost:28181/management/v2/catalog/dataset/request" -s | jq


# negotiate
curl -H 'Content-Type: application/json' -d @extensions/policy/policy-time-interval/5-negotiate-contract.json  -X POST "http://localhost:28181/management/v2/contractnegotiations" -s | jq

```