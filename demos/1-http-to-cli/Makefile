.PHONY: help up run fixed broken logs down

.DEFAULT_GOAL := help

COMPOSE := docker compose

help:
	@echo "make up      Build the Java client, then start Jaeger and the Go report API"
	@echo "make run     Send one request without env propagation"
	@echo "make fixed   Send one request with env propagation"
	@echo "make broken  Alias for make run"
	@echo "make logs    Follow API and CLI diagnostic output"
	@echo "make down    Stop this demo's containers"

up:
	$(COMPOSE) build api client
	$(COMPOSE) up --detach jaeger api

run:
	$(COMPOSE) run --rm client

fixed:
	$(COMPOSE) run --rm --env 'REPORT_API_URL=http://api:8080/report?propagate=true' client

broken: run

logs:
	$(COMPOSE) logs --follow api

down:
	$(COMPOSE) down --remove-orphans
