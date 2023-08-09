import json

# parses the response data returned from /actuator/mappings and creates the routes.txt file
# in project root -- requires actuator-response-data.json in order to function
if __name__ == "__main__":
    with open('actuator-response-data.json', 'r') as f:
        servs = [serv['predicate'] for serv in json.load(f)['contexts']['application']['mappings']['dispatcherServlets']['dispatcherServlet']]
        
        with open('../routes.txt', 'w') as r:
            for route in servs:
                r.write(f"{route}\n")
 
