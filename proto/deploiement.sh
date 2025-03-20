#!/bin/bash

# Définition des variables
PROJECT_DIR="C:\Users\Maxime\Documents\apprendmaven\ClinicPro"
JAR_NAME="C:\Users\Maxime\Documents\apprendmaven\ClinicPro\proto\xmart-city-backend\target\xmart-zity-backend-1.0-SNAPSHOT-jar-with-dependencies.jar"  # Remplace par le vrai nom du JAR généré
VM_BACKEND_USER="toto"
VM_BACKEND_IP="172.31.252.147"  # Adresse IP de la VM backend
VM_BACKEND_DIR="/home/toto"
VM_BDD_USER="toto"
VM_BDD_IP="172.31.253.249"  # Adresse IP de la VM BDD
FRONTEND_DIR="/chemin/vers/le/frontend"

echo " Déploiement en cours..."

# 1️Compilation et packaging du backend avec Maven
echo "Compilation du projet Maven..."
cd "$PROJECT_DIR" || exit
mvn clean package -DskipTests

# Vérifier si le build a réussi
if [ $? -ne 0 ]; then
    echo " Erreur lors du build Maven !"
    exit 1
fi

# Copie du JAR vers la VM backend
echo "Transfert du backend vers la VM backend..."
scp "$PROJECT_DIR/target/$JAR_NAME" "$VM_BACKEND_USER@$VM_BACKEND_IP:$VM_BACKEND_DIR"

# Vérifier si la copie a réussi
if [ $? -ne 0 ]; then
    echo " Erreur lors du transfert du JAR !"
    exit 1
fi

# Lancer le backend sur la VM backend
echo " Démarrage du backend sur la VM backend..."
ssh "$VM_BACKEND_USER@$VM_BACKEND_IP" << EOF
    pkill -f "$JAR_NAME" || true  # Arrêter le process s'il existe déjà
    nohup java -jar "$VM_BACKEND_DIR/$JAR_NAME" > "$VM_BACKEND_DIR/logs.txt" 2>&1 &
    echo " Backend démarré sur la VM backend"
EOF

#  Vérifier la connexion à la BDD (optionnel)
echo " Vérification de la connexion à la base de données..."
ssh "$VM_BDD_USER@$VM_BDD_IP" << EOF
    systemctl is-active --quiet mysql && echo " Base de données en ligne" || echo " Base de données hors ligne"
EOF

echo "Démarrage du frontend..."
cd "$PROJECT_DIR"
mvn clean package -pl xmart-frontend
java -jar "$PROJECT_DIR/xmart-frontend/target/xmart-frontend-1.0-SNAPSHOT-jar-with-dependencies.jar" &

echo "Déploiement terminé avec succès !"
