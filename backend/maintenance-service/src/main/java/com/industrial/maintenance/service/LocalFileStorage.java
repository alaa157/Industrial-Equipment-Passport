package com.industrial.maintenance.service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileStorage {
private static final long MAX_SIZE=15*1024*1024L;
private static final Set<String> ALLOWED=Set.of("image/jpeg","image/png","application/pdf","text/plain");
private final Path root;

public LocalFileStorage(@Value("${file.storage-path:/app/storage}") String path){
root=Paths.get(path).toAbsolutePath().normalize();
try{Files.createDirectories(root.resolve("maintenance"));}catch(IOException ex){throw new IllegalStateException(ex);}
}

public Stored store(MultipartFile file)throws IOException{
if(file==null||file.isEmpty())throw new IllegalArgumentException("File is empty");
if(file.getSize()>MAX_SIZE)throw new IllegalArgumentException("File exceeds the 15 MB limit");
if(file.getContentType()==null||!ALLOWED.contains(file.getContentType()))throw new IllegalArgumentException("File type is not allowed");

String original=Path.of(file.getOriginalFilename()==null?"file":file.getOriginalFilename()).getFileName().toString();
if(original.contains(".."))throw new IllegalArgumentException("Invalid filename");
String ext="";
int dot=original.lastIndexOf('.');
if(dot>=0)ext=original.substring(dot).toLowerCase();

String filename=UUID.randomUUID()+ext;
Path directory=root.resolve("maintenance").normalize();
Path target=directory.resolve(filename).normalize();
if(!target.startsWith(directory))throw new IllegalArgumentException("Invalid path");

Files.createDirectories(directory);
try(InputStream in=file.getInputStream()){Files.copy(in,target,StandardCopyOption.REPLACE_EXISTING);}
return new Stored(original,filename,file.getContentType(),file.getSize(),root.relativize(target).toString());
}

public Path load(String path){
Path result=root.resolve(path).normalize();
if(!result.startsWith(root))throw new IllegalArgumentException("Invalid path");
return result;
}

public void delete(String path){
try{Files.deleteIfExists(load(path));}catch(IOException ex){throw new IllegalStateException(ex);}
}

public record Stored(String originalFilename,String storedFilename,String contentType,long sizeBytes,String relativePath){}
}
