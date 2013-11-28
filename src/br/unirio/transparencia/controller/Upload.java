package br.unirio.transparencia.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Upload extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
    	
  
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        String documento="";
        
        List<BlobKey> blobKeyList = blobs.get("selo");
      
        if (blobKeyList!=null && blobKeyList.size()>0)
        {
        	documento="selo";
        }
        else
        {
        	blobKeyList = blobs.get("resultado");
        	if (blobKeyList!=null && blobKeyList.size()>0)
            {
            	documento="resultado";
            }	
        }
        
        for(BlobKey blobKey: blobKeyList)
        if (blobKey == null) {
            res.sendRedirect("/");
        } else {
        	
        	req.getSession().setAttribute(documento,"/serve?blob-key=" + blobKey.getKeyString());
        	res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
        }
    }
}
