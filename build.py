import subprocess

# 서비스 목록 및 Dockerfile 위치를 정의
services = [
    {"name": "user-service", "dockerfile_path": "user-service/Dockerfile"},
    {"name": "product-service", "dockerfile_path": "product-service/Dockerfile"},
    {"name": "order-service", "dockerfile_path": "order-service/Dockerfile"},
    {"name": "wish-service", "dockerfile_path": "wish-service/Dockerfile"},
    {"name": "eureka-service", "dockerfile_path": "eureka-service/Dockerfile"},
    {"name": "gateway-service", "dockerfile_path": "gateway-service/Dockerfile"},
]

# 각 서비스를 빌드
def build_images():
    for service in services:
        try:
            print(f"Building Docker image for {service['name']}...")
            subprocess.run(
                ["docker", "buildx", "build", "--platform", "linux/amd64", "-f", service["dockerfile_path"], "-t", f"{service['name']}:latest", ".", "--load" ],
                check=True
            )
            print(f"{service['name']} image built successfully.")
        except subprocess.CalledProcessError as e:
            print(f"Error building image for {service['name']}: {e}")
            return False
    return True

# docker-compose 실행
def run_docker_compose():
    try:
        print("Starting services with docker-compose...")
        subprocess.run(["docker-compose", "up", "-d"], check=True)
        print("Services started successfully.")
    except subprocess.CalledProcessError as e:
        print(f"Error starting services with docker-compose: {e}")

# main 함수 정의
def main():
    if build_images():
        run_docker_compose()
    else:
        print("Failed to build one or more images. Aborting docker-compose.")

# 스크립트를 직접 실행할 때만 main() 실행
if __name__ == "__main__":
    main()
