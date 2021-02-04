# Progetto5B-HeartBeat
 
Autore: Mineo Raffaele matr. 1000008826

Progetto 5 (Ping-Ack) in versione B (WebFlux) con strategia di Health-Check di tipo HeartBeat

Container:
- PingAckFaultDetector: micro-servizio che periodicamente esegua la richiesta GET /ping su una lista di host
- TestClient: client con endpoint /ping per testare le richieste di vita
- TestHeartBeat: client con endpoint /ping per testare strategia di Health-Check

Specifiche:

Esempio host:
["orders", "products", "invoicing", ...]

Scrivere un micro-servizio separato che sappia rispondere a GET /ping genericamente come da paragrafo 'Specifiche comuni'.

Ovvero, un servizio che alla GET /ping risponda con:
{
"serviceStatus": "up|down",
"dbStatus": "up|down"
}

Scegliere up o down in maniera casuale: up scelto se rand.uniform(0, 1) < 0.7 (70 % dei casi) .

Se la risposta da uno degli host riporta almeno un "down", mandare su Kafka, nel topic logging la risposta ricevuta con il messaggio:
key: service_down
value: {
time: UnixTimeStamp
status: JsonResponse
service: hostname
}

Se il service non e' proprio raggiungibile lo status diventa:
{
"serverUnavailable": // messaggio di errore come "Connection refused",
// "No route to host", "Timeout"...
}

Nota bene: la lista degli host deve essere configurata da variabile d'ambiente.

Ping endpoint 2 (heart-beat mode)

Ogni micro-servizio periodicamente (periodo definita con variabile d'ambiente) esegue una richiesta POST /ping con body:

{
"service": "serviceName",
"serviceStatus": "up|down",
"dbStatus": "up|down"
}

Le due key, rappresentano, rispettivamente, uno stato generale del servizio e lo stato di connessione al database. La key service, invece, riporta
il nome del micro-service.

#K8s minikube use

docker build -t pingackfd:v1 -f PingAckFaultDetector/Dockerfile ./pingackfd

docker build -t testclient:v1 -f TestClient/Dockerfile ./testclient

docker build -t testheartbeat:v1 -f TestHeartBeat/Dockerfile ./testheartbeat

kubectl apply -f k8s/pingackfd

kubectl apply -f k8s/testclient

kubectl apply -f k8s/testheartbeat

kubectl apply -f k8s/kafka
