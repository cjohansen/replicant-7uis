node_modules:
	npm install

tailwind: node_modules
	npx @tailwindcss/cli -i ./src/main.css -o ./resources/public/styles.css --watch

shadow: node_modules
	npx shadow-cljs watch app

.PHONY: tailwind shadow
