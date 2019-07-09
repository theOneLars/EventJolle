#!/usr/bin/env bash
cd "${0%/*}"
docker stop sailing-events || true &&
docker rm sailing-events || true &&
docker build -t sailing-events . &&
docker run -d --name sailing-events -p 5432:5432 sailing-events
