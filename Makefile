.PHONY: install dev build export
.DEFAULT_GOAL := dev

install:
	npm ci

dev:
	npm run dev

build:
	npm run build

export:
	npm run export
