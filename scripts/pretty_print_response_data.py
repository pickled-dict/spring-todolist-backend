import json

if __name__ == "__main__":
    with open('actuator-response-data.json', 'r') as f:
        data = json.load(f)
        print(json.dumps(data, indent=2))

