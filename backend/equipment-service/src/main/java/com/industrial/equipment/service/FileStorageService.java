package com.industrial.equipment.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
private static final Set<String> ALLOWED_TYPES=Set.of(
"image/jpeg","image/png","application/pdf","text/plain",
"application/vnd.openxmlformats-officedocument.wordprocessingml.document"
);
private static final long MAX_SIZE=15*1024*1024L;
private final Path root;

public FileStorageService(@Value("${file.storage-path:./storage}") String path){
root=Paths.get(path).toAbsolutePath().normalize();
try{Files.createDirectories(root);}catch(IOException ex){throw new IllegalStateException("Cannot initialize file storage",ex);}
}

public StoredFile store(MultipartFile file,String category){
validate(file);
String original=Path.of(file.getOriginalFilename()==null?"file":file.getOriginalFilename()).getFileName().toString();
String extension="";
int index=original.lastIndexOf('.');
if(index>=0)extension=original.substring(index).toLowerCase();
String filename=UUID.randomUUID()+extension;
Path directory=root.resolve(category).normalize();
Path target=directory.resolve(filename).normalize();
if(!target.startsWith(directory))throw new IllegalArgumentException("Invalid file path");
try{
Files.createDirectories(directory);
try(InputStream in=file.getInputStream()){Files.copy(in,target,StandardCopyOption.REPLACE_EXISTING);}
return new StoredFile(original,filename,file.getContentType(),file.getSize(),root.relativize(target).toString());
}catch(IOException ex){throw new IllegalStateException("File storage failed",ex);}
}

public Path load(String relativePath){
Path target=root.resolve(relativePath).normalize();
if(!target.startsWith(root))throw new IllegalArgumentException("Invalid file path");
return target;
}

public void delete(String relativePath){
try{Files.deleteIfExists(load(relativePath));}catch(IOException ex){throw new IllegalStateException("File deletion failed",ex);}
}

private void validate(MultipartFile file){
if(file==null||file.isEmpty())throw new IllegalArgumentException("File is empty");
if(file.getSize()>MAX_SIZE)throw new IllegalArgumentException("File exceeds the 15 MB limit");
String type=file.getContentType();
if(type==null||!ALLOWED_TYPES.contains(type))throw new IllegalArgumentException("File type is not allowed");
String name=file.getOriginalFilename();
if(name==null||name.isBlank()||name.contains(".."))throw new IllegalArgumentException("Invalid filename");
}

public record StoredFile(String originalFilename,String storedFilename,String contentType,long sizeBytes,String relativePath){}
}
