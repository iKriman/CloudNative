package com.example.bdget.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.File;
import java.nio.file.Paths;

@Service
public class AWSS3ServiceImpl implements AWSS3Service {

    // Nombre del bucket
    private final String NOMBRE_BUCKET = "mi-bucket-guias-despacho";

    @Override
    public String subirGuia(File archivoPdf, String transportista, String fecha) {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        String sessionToken = System.getenv("AWS_SESSION_TOKEN");

        // 2. Creamos el cliente
        S3Client s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(accessKey, secretKey, sessionToken)
                ))
                .build();

        // 3. Armamos la estructura de carpetas
        String rutaEstructuradaS3 = fecha + "/" + transportista + "/" + archivoPdf.getName();

        // 4. Preparamos la subida del archivo a AWS
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(NOMBRE_BUCKET)
                .key(rutaEstructuradaS3)
                .build();

        // 5. Subimos el archivo físico
        s3Client.putObject(putObjectRequest, Paths.get(archivoPdf.getAbsolutePath()));

        // Retornamos la ruta final para guardarla en la base de datos Oracle
        return rutaEstructuradaS3;
    }
}