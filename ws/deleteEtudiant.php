<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
include_once '../connexion/Connexion.php';

$conn = (new Connexion())->getConnexion();
$id   = intval($_POST['id']);

$stmt = $conn->prepare("DELETE FROM Etudiant WHERE id = :id");
$stmt->execute([':id' => $id]);

echo json_encode([
    "succes"  => true,
    "message" => "Étudiant supprimé avec succès",
    "lignes"  => $stmt->rowCount()
]);
?>