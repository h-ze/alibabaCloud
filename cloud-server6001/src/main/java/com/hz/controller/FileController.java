package com.hz.controller;

import com.hz.service.UserFileService;
import com.common.entity.User;
import com.common.entity.UserFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文件处理模块
 */
@Controller
@RequestMapping("file")
public class FileController {


    @Autowired
    private UserFileService userFileService;

    //springboot里的上传文件大小默认是10M 超过10M会报错(可以在配置文件中设置)

    @GetMapping("findAllJSON")
    @ResponseBody
    public List<UserFile> showAllFiles(HttpSession session, HttpServletResponse response, Model model){
        User user = (User)session.getAttribute("user");

        List<UserFile> uSerFileById = userFileService.findUSerFileById(user.getId());
        //response.set
        //model.addAttribute("files",uSerFileById);
        return uSerFileById;
    }

    /**
     * 基本实现上传功能
     * @param multipartFile
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("uploadFile")
    public String uploadFile(MultipartFile multipartFile, HttpServletRequest request,HttpSession session) throws IOException {
        System.out.println(multipartFile);
        User user = (User) session.getAttribute("user");
        String oldFileName = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();
        String contentType = multipartFile.getContentType();
        String type = multipartFile.getContentType();

        String path = ResourceUtils.getURL("classpath:").getPath()+"static/files";
        //日期目录创建
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //String realPath = request.getServletContext().getRealPath("/files");
        System.out.println(path);
        File file = new File(path,format);
        if (!file.exists()) {
            file.mkdir();
        }
        //修改文件名
        String newFileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS")+UUID.randomUUID().toString();
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String newFileName = newFileNamePrefix+"."+extension;

        multipartFile.transferTo(new File(file,newFileName));


        UserFile userFile = new UserFile();
        userFile.setOldFileName(oldFileName);
        userFile.setNewFileName(newFileName);
        userFile.setPath("/files/"+format);
        userFile.setSize(String.valueOf(size));
        userFile.setType(type);
        //userFile.setUploadTime();
        userFile.setExt(extension);
        userFile.setUserId(user.getId());
        userFileService.saveFileMessage(userFile);

        return "redirect:/file/showAllFile";
    }

    @GetMapping("downloadFile")
    public void downloadFile(String openStyle,String id, HttpServletResponse response) throws IOException {
        openStyle = openStyle == null ? "attachment" : openStyle;

        UserFile fileById = userFileService.findFileById(id);

        //更新下载次数

        if ("attachment".equals(openStyle)) {
            fileById.setDowncounts(fileById.getDowncounts()+1);
            userFileService.updateFileCount(fileById);
        }

        //根据文件名
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "static"+fileById.getPath();

        System.out.println(realPath);
        File file = new File(realPath, fileById.getNewFileName());


        FileInputStream fileInputStream = new FileInputStream(file);
        response.setHeader("content-disposition",openStyle+";fileName="+ URLEncoder.encode(fileById.getOldFileName(),"UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(fileInputStream,outputStream);
        IOUtils.closeQuietly(fileInputStream);
        IOUtils.closeQuietly(outputStream);

        /*int len = 0;
        byte[] bytes = new byte[1024];
        while (true){
            len = fileInputStream.read(bytes);
            if (len==-1){
                break;
            }
            outputStream.write(bytes,0,len);
            fileInputStream.close();
            outputStream.close();

        }*/
    }

    /**
     * 基本实现上传功能
     * @param multipartFile
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("upload")
    public String uploadFiles(MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        System.out.println(multipartFile);
        String path = ResourceUtils.getURL("classpath:").getPath()+"static/files";
        //日期目录创建
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //String realPath = request.getServletContext().getRealPath("/files");
        System.out.println(path);
        File file = new File(path,format);
        if (!file.exists()) {
            file.mkdir();
        }
        //修改文件名
        String newFileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS")+UUID.randomUUID().toString();
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String newFileName = newFileNamePrefix+"."+extension;

        multipartFile.transferTo(new File(file,newFileName));

        return "redirect/upload";
    }

    @GetMapping("showAllFile")
    public String showAllFile(HttpSession session, HttpServletResponse response, Model model){
        User user = (User)session.getAttribute("user");

        List<UserFile> uSerFileById = userFileService.findUSerFileById(1);
        //response.set
        model.addAttribute("files",uSerFileById);
        return "showAllFile";
    }

    /**
     * 基本实现下载功能
     * @param fileName
     * @param response
     * @throws IOException
     */
    @GetMapping("download")
    public void download(String fileName, HttpServletResponse response) throws IOException {

        //根据文件名
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "static/files";

        System.out.println(realPath);
        File file = new File(realPath, fileName);


        FileInputStream fileInputStream = new FileInputStream(file);
        response.setHeader("content-disposition","attachment;fileName="+fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(fileInputStream,outputStream);
        IOUtils.closeQuietly(fileInputStream);
        IOUtils.closeQuietly(outputStream);
        /*int len = 0;
        byte[] bytes = new byte[1024];
        while (true){
            len = fileInputStream.read(bytes);
            if (len==-1){
                break;
            }
            outputStream.write(bytes,0,len);
            fileInputStream.close();
            outputStream.close();

        }*/
    }

}
