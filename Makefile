start_game:
	./gradlew build && \
	docker-compose -f ./docker-compose.yml build --no-cache && \
	docker-compose -f ./docker-compose.yml up --abort-on-container-exit