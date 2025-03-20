#!/bin/bash

# Définition des variables
CHEMIN_DU_PROJET="/c/Users/Maxime/Documents/apprendmaven/ClinicPro/proto"
JAR_BACKEND="xmart-zity-backend-1.0-SNAPSHOT-jar-with-dependencies.jar"
VM_BACKEND_USER="toto"
VM_BACKEND_IP="172.31.252.147"
VM_BACKEND_DIR="/home/toto"
VM_BDD_USER="toto"
VM_BDD_IP="172.31.253.249"

CHEMIN_FRONTEND="$CHEMIN_DU_PROJET/xmart-frontend"

echo "Déploiement en cours..."

# Compilation et packaging du backend avec Maven
echo " Compilation du projet Maven..."
cd "$CHEMIN_DU_PROJET" || exit 1
mvn clean package -DskipTests

# Vérifier que mvn package a bien réussi
if [ $? -ne 0 ]; then
    echo "Erreur (mvn package)"
    exit 1
fi

# Copie du JAR vers la VM backend
echo "Transfert du backend vers la VM backend"

#Utilisation de la clé ssh pour éviter d'entrer le mot dee passe.
scp -i ~/.ssh/id_ed25519 "$CHEMIN_DU_PROJET/xmart-city-backend/target/$JAR_BACKEND" "$VM_BACKEND_USER@$VM_BACKEND_IP:$VM_BACKEND_DIR"

# Vérifier si la copie a réussi
if [ $? -ne 0 ]; then
    echo "Erreur lors du transfert du JAR du backend!"
    exit 1
fi

# Lancemen du backend sur la VM backend
echo "Démarrage du backend sur la VM backend..."
ssh -i ~/.ssh/id_ed25519 "$VM_BACKEND_USER@$VM_BACKEND_IP" << EOF
    pkill -f "$JAR_BACKEND" || true  # Arrêter le process s'il existe déjà
    nohup java -jar "$VM_BACKEND_DIR/$JAR_BACKEND" > "$VM_BACKEND_DIR/logs.txt" 2>&1 &

     # Ouvre un terminal interactif pour voir les logs
    gnome-terminal -- bash -c "tail -f $VM_BACKEND_DIR/logs.txt; exec bash"

    echo "Backend démarré sur la VM backend"
EOF

# 4Vérification de la connexion à la BDD
echo "Vérification de la connexion à la base de données..."
ssh "$VM_BDD_USER@$VM_BDD_IP" << EOF
    systemctl is-active --quiet mysql && echo "Base de données en ligne" || echo "Base de données hors ligne"
EOF

# Démarrage du frontend
echo "Démarrage du frontend..."
#cd "$FRONTEND_DIR" || exit 1
#mvn clean package -am -pl xmart-frontend
#
#if [ $? -ne 0 ]; then
#    echo " Erreur lors du build du frontend !"
#    exit 1
#fi

java -jar "$CHEMIN_FRONTEND/target/xmart-frontend-1.0-SNAPSHOT-jar-with-dependencies.jar" &

echo "Déploiement terminé avec succès !"
