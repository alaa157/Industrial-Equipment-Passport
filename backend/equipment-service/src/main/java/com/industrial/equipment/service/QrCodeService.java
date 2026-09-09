package com.industrial.equipment.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

@Service
public class QrCodeService {
public byte[] generate(String value){
try{
BitMatrix matrix=new QRCodeWriter().encode(value,BarcodeFormat.QR_CODE,500,500);
ByteArrayOutputStream output=new ByteArrayOutputStream();
MatrixToImageWriter.writeToStream(matrix,"PNG",output);
return output.toByteArray();
}catch(Exception ex){throw new IllegalStateException("QR generation failed",ex);}
}
}
