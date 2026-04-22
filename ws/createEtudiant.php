<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
include_once '../connexion/Connexion.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(["erreur" => "Méthode non autorisée"]);
    exit;
}

$conn = (new Connexion())->getConnexion();

$nom    = htmlspecialchars(trim($_POST['nom']));
$prenom = htmlspecialchars(trim($_POST['prenom']));
$ville  = htmlspecialchars(trim($_POST['ville']));
$sexe   = htmlspecialchars(trim($_POST['sexe']));

if (empty($nom) || empty($prenom) || empty($ville) || empty($sexe)) {
    echo json_encode(["erreur" => "Tous les champs sont obligatoires"]);
    exit;
}

$stmt = $conn->prepare("INSERT INTO Etudiant (nom, prenom, ville, sexe) 
                        VALUES (:nom, :prenom, :ville, :sexe)");
$stmt->execute([
    ':nom'    => $nom,
    ':prenom' => $prenom,
    ':ville'  => $ville,
    ':sexe'   => $sexe
]);

$nouveauId = $conn->lastInsertId();
$stmt2 = $conn->prepare("SELECT * FROM Etudiant WHERE id = :id");
$stmt2->execute([':id' => $nouveauId]);
$nouvelEtudiant = $stmt2->fetch();

echo json_encode([
    "succes"   => true,
    "message"  => "Étudiant inscrit avec succès",
    "etudiant" => $nouvelEtudiant
]);
?>