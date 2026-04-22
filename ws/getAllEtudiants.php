<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
include_once '../connexion/Connexion.php';

$conn = (new Connexion())->getConnexion();

$stmt = $conn->prepare("SELECT * FROM Etudiant ORDER BY nom ASC");
$stmt->execute();
$etudiants = $stmt->fetchAll();

echo json_encode([
    "succes"    => true,
    "total"     => count($etudiants),
    "etudiants" => $etudiants
]);
?>