package com.example.bdget.service;

import java.io.File;

public interface AWSS3Service {
    String subirGuia(File archivoPdf, String transportista, String fecha);
}