package br.unirio.transparencia.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Upload extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeyList = blobs.get("myFile");
        for(BlobKey blobKey: blobKeyList)
        if (blobKey == null) {
            res.sendRedirect("/");
        } else {
        	req.getSession().setAttribute("documento",blobKey.getKeyString());
        	
            res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
        }
    }
}
