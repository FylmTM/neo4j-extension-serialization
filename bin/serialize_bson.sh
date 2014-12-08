#!/bin/bash

QUERY=query.json

curl -i -XPOST \
    -o output.log \
    --data "@$QUERY" \
    -H "Content-Type: application/json" \
    -w "
    time_connect=%{time_connect}
    time_start_transfer=%{time_starttransfer}
    time_total=%{time_total}
    " \
    http://127.0.0.1:7474/unmanaged/bson/transaction/commit

