<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
include_once '../connexion/Connexion.php';

$conn = (new Connexion())->getConnexion();

$id     = intval($_POST['id']);
$nom    = htmlspecialchars(trim($_POST['nom']));
$prenom = htmlspecialchars(trim($_POST['prenom']));
$ville  = htmlspecialchars(trim($_POST['ville']));
$sexe   = htmlspecialchars(trim($_POST['sexe']));

$stmt = $conn->prepare("UPDATE Etudiant 
                        SET nom=:nom, prenom=:prenom, ville=:ville, sexe=:sexe 
                        WHERE id=:id");
$stmt->execute([
    ':id'     => $id,
    ':nom'    => $nom,
    ':prenom' => $prenom,
    ':ville'  => $ville,
    ':sexe'   => $sexe
]);

echo json_encode([
    "succes"  => true,
    "message" => "Étudiant mis à jour avec succès",
    "lignes"  => $stmt->rowCount()
]);
?>