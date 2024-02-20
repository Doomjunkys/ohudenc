package org.itkk.udf.starter.file.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreProperties;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import org.itkk.udf.starter.file.db.DbFileConstant;
import org.itkk.udf.starter.file.db.DbFileProperties;
import org.itkk.udf.starter.file.db.IGenPreviewPdf;
import org.itkk.udf.starter.file.db.dto.DbFileInfoDto;
import org.itkk.udf.starter.file.db.entity.DbFileInfoEntity;
import org.itkk.udf.starter.file.db.repository.IDbFileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class DbFileService {

    /**
     * 当前环境
     */
    @Value("${spring.profiles.active}")
    private String profilesActive;

    /**
     * 当前应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * coreProperties
     */
    @Autowired
    private CoreProperties coreProperties;

    /**
     * iDbFileInfoRepository
     */
    @Autowired
    private IDbFileInfoRepository iDbFileInfoRepository;

    /**
     * dbFileProperties
     */
    @Autowired
    private DbFileProperties dbFileProperties;

    /**
     * iGenPreviewPdf
     */
    @Autowired(required = false)
    private IGenPreviewPdf iGenPreviewPdf;

    /**
     * 刷新文件长度
     *
     * @param fileId fileId
     * @param userId userId
     */
    public void refreshFileSize(String fileId, String userId) {
        try {
            iDbFileInfoRepository.updateById(
                    new DbFileInfoEntity()
                            .setId(fileId)
                            .setFileSize(Files.size(Paths.get(getPhysicalPath(fileId))))
                            .setUpdateBy(userId)
                            .setUpdateDate(new Date())
            );
        } catch (IOException e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * 截取文件名称
     *
     * @param fileName fileName
     * @return String
     */
    private String subFileName(String fileName) {
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (Math.max(winSep, unixSep));
        if (pos != -1) {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        return fileName;
    }

    /**
     * 文件上传
     *
     * @param rootPathCode      根路径代码
     * @param genPreviewPdfFile 是否生成预览文件
     * @param businessPath      业务路径
     * @param fileName          文件名称
     * @param contentType       上下文类型
     * @param inputStream       流
     * @param userId            用户名
     * @return 文件DTO
     * @throws IOException IOException
     */
    public DbFileInfoDto upload(String rootPathCode, Boolean genPreviewPdfFile, String businessPath, String fileName, String contentType, InputStream inputStream, String userId) throws IOException {
        //处理文件名称
        fileName = subFileName(fileName);
        //判空
        if (StringUtils.isBlank(rootPathCode)) {
            throw new ParameterValidException("rootPathCode不能为空");
        }
        if (MapUtils.isEmpty(dbFileProperties.getMapping()) || !dbFileProperties.getMapping().containsKey(rootPathCode)) {
            throw new ParameterValidException("文件路径映射" + rootPathCode + "不存在");
        }
        //构造必要属性
        final String[] dateArr = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).split("-");
        final String fileId = UUID.randomUUID().toString(); //文件ID
        final String physicalRootPath = dbFileProperties.getMapping().get(rootPathCode); //物理根路径
        final String physicalRelativePath = "store/" + dateArr[0] + "/" + dateArr[1] + "/" + dateArr[2] + "/"; //物理相对路径
        final String fileSuffix = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null; //文件后缀
        final String physicalFileName = fileId + (StringUtils.isNotBlank(fileSuffix) ? "." + fileSuffix : null); //物理文件名称
        final String physicalAbsolutePath = physicalRootPath + physicalRelativePath; //物理绝对路径(目录)
        final String physicalAbsoluteFilePath = physicalAbsolutePath + physicalFileName; //物理绝对路径(文件)
        //判断文件后缀是否能上传
        if (StringUtils.isNotBlank(dbFileProperties.getFileUploadSupportType())) {
            if (!dbFileProperties.getFileUploadSupportType().toLowerCase().contains(fileSuffix.toLowerCase())) {
                throw new ParameterValidException("不能上传[" + fileSuffix + "]类型的文件");
            }
        }
        //文件落地
        try (
                InputStream isi = inputStream
        ) {
            if (!Files.exists(Paths.get(physicalAbsolutePath))) {
                Files.createDirectories(Paths.get(physicalAbsolutePath));
            }
            Files.copy(isi, Paths.get(physicalAbsoluteFilePath), StandardCopyOption.REPLACE_EXISTING);
        }
        //判断是否要生成PDF预览文件
        String previewPdfFileId = null;
        if (genPreviewPdfFile != null && genPreviewPdfFile) {
            //判断是否是支持的上下文类型
            if (CoreConstant.OFFICE_CONVERT_PDF_SUPPORT_CONTENT_TYPE.contains(contentType)) {
                //判断转换器是否有实现
                if (iGenPreviewPdf != null) {
                    //生成预览文件
                    DbFileInfoDto previewPdfFileInfoDto = upload(rootPathCode, false, businessPath, fileName + ".pdf", CoreConstant.CONTENT_TYPE_PDF, new ByteArrayInputStream(iGenPreviewPdf.convert(physicalAbsoluteFilePath)), userId);
                    //构造必要参数
                    previewPdfFileId = previewPdfFileInfoDto.getId();
                }
            }
        }
        //存储数据
        DbFileInfoEntity dbFileInfoEntity = new DbFileInfoEntity()
                .setId(fileId)
                .setApplicationName(applicationName)
                .setProfilesActive(profilesActive)
                .setRootPathCode(rootPathCode)
                .setPhysicalRelativePath(physicalRelativePath)
                .setPhysicalFileName(physicalFileName)
                .setFileName(fileName)
                .setContentType(contentType)
                .setFileSize(Files.size(Paths.get(physicalAbsoluteFilePath)))
                .setFileSuffix(fileSuffix)
                .setBusinessPath(businessPath)
                .setPreviewPdfFileId(previewPdfFileId)
                .setCreateBy(userId)
                .setCreateDate(new Date())
                .setUpdateBy(userId)
                .setUpdateDate(new Date());
        iDbFileInfoRepository.insert(dbFileInfoEntity);
        //返回
        return get(fileId);
    }

    /**
     * 下载
     *
     * @param id     文件ID
     * @param width  宽度
     * @param height 高度
     * @param os     输出流
     * @throws IOException IOException
     */
    public void download(String id, Integer width, Integer height, OutputStream os) throws IOException {
        //获得文件信息
        DbFileInfoEntity fileInfo = this.getEntity(id);
        //判空
        if (fileInfo == null) {
            throw new ParameterValidException("文件ID:" + id + "对应的文件不存在");
        }
        //获得文件路径
        Path path = Paths.get(dbFileProperties.getMapping().get(fileInfo.getRootPathCode()) + fileInfo.getPhysicalRelativePath() + fileInfo.getPhysicalFileName());
        //缩略图处理
        if (DbFileConstant.CONTENT_TYPE_JPEG.equals(fileInfo.getContentType()) || DbFileConstant.CONTENT_TYPE_PNG.equals(fileInfo.getContentType())) {
            if (width != null && height != null) {
                Path thumbnailsPath = Paths.get(path.getParent().toString() + "/" + width + "x" + height + "/" + fileInfo.getPhysicalFileName());
                if (!Files.exists(thumbnailsPath)) {
                    try {
                        Files.createDirectories(thumbnailsPath.getParent());
                    } catch (Exception e) {
                        log.warn("{} --> {} 创建文件目录失败,错误类型为 : {}", CoreUtil.getTraceId(), thumbnailsPath.getParent(), e.getClass().getName());
                    }
                    Files.createFile(thumbnailsPath);
                    Thumbnails.of(path.toFile()).size(width, height).keepAspectRatio(false).toFile(thumbnailsPath.toFile());
                }
                path = thumbnailsPath;
            }
        }
        //输出文件
        try (
                OutputStream osi = new BufferedOutputStream(os);
                BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(path));
        ) {
            byte[] buff = new byte[bis.available()];
            bis.read(buff);
            osi.write(buff);
            osi.flush();
        }
    }

    /**
     * 删除文件以及信息
     *
     * @param id id
     */
    public void delete(String id) throws IOException {
        //获得文件信息
        DbFileInfoEntity fileInfo = this.getEntity(id);
        //判空
        if (fileInfo == null) {
            return;
        }
        //删除文件
        Path path = Paths.get(dbFileProperties.getMapping().get(fileInfo.getRootPathCode()) + fileInfo.getPhysicalRelativePath() + fileInfo.getPhysicalFileName());
        Files.delete(path);
        //删除数据
        iDbFileInfoRepository.deleteById(id);
        //如果有预览文件,也删除预览文件
        if (StringUtils.isNotBlank(fileInfo.getPreviewPdfFileId())) {
            this.delete(fileInfo.getPreviewPdfFileId());
        }
    }

    /**
     * 批量获得文件信息
     *
     * @param ids ids
     * @return Map<String, DbFileInfoDto>
     */
    public Map<String, DbFileInfoDto> getBatch(List<String> ids) {
        Map<String, DbFileInfoDto> rs = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            ids.forEach(id -> {
                if (StringUtils.isNotBlank(id)) {
                    rs.put(id, this.get(id));
                }
            });
        }
        return rs;
    }

    /**
     * 获得文件信息
     *
     * @param id id
     * @return DbFileInfoDto
     */
    public DbFileInfoDto get(String id) {
        DbFileInfoDto dbFileInfoDto = null;
        DbFileInfoEntity dbFileInfoEntity = this.getEntity(id);
        if (dbFileInfoEntity != null) {
            dbFileInfoDto = new DbFileInfoDto();
            CoreUtil.copyPropertiesIgnoreNull(this.getEntity(id), dbFileInfoDto);
            buildOfficeOnlineLink(dbFileInfoDto);
        }
        return dbFileInfoDto;
    }

    /**
     * 返回文件
     *
     * @param id
     * @return 文件
     */
    public File getFile(String id) {
        return new File(this.getPhysicalPath(id));
    }

    /**
     * 构造OfficeOnline相关链接
     *
     * @param dbFileInfoDto dbFileInfoDto
     */
    private void buildOfficeOnlineLink(DbFileInfoDto dbFileInfoDto) {
        //配置属性判空
        if (StringUtils.isNotBlank(dbFileProperties.getOfficeOnlineHost()) || StringUtils.isNotBlank(dbFileProperties.getOfficeOnlineWopiSrcHost())) {
            //文件类型判断
            if (CoreConstant.CONTENT_TYPE_DOC.equals(dbFileInfoDto.getContentType()) || CoreConstant.CONTENT_TYPE_DOCX.equals(dbFileInfoDto.getContentType())) {
                //WORD
                dbFileInfoDto.setViewLink(dbFileProperties.getOfficeOnlineHost() + "/wv/wordviewerframe.aspx?WOPISrc=" + dbFileProperties.getOfficeOnlineWopiSrcHost() + "/wopi/files/" + dbFileInfoDto.getId());
                dbFileInfoDto.setEmbedViewLink(dbFileProperties.getOfficeOnlineHost() + "/wv/wordviewerframe.aspx?embed=1&WOPISrc=" + dbFileProperties.getOfficeOnlineWopiSrcHost() + "/wopi/files/" + dbFileInfoDto.getId());
            } else if (CoreConstant.CONTENT_TYPE_XLS.equals(dbFileInfoDto.getContentType()) || CoreConstant.CONTENT_TYPE_XLSX.equals(dbFileInfoDto.getContentType())) {
                //EXCEL
                dbFileInfoDto.setViewLink(dbFileProperties.getOfficeOnlineHost() + "/x/_layouts/xlviewerinternal.aspx?WOPISrc=" + dbFileProperties.getOfficeOnlineWopiSrcHost() + "/wopi/files/" + dbFileInfoDto.getId());
                dbFileInfoDto.setEmbedViewLink(dbFileProperties.getOfficeOnlineHost() + "/x/_layouts/xlembed.aspx?WOPISrc=" + dbFileProperties.getOfficeOnlineWopiSrcHost() + "/wopi/files/" + dbFileInfoDto.getId());
            } else if (CoreConstant.CONTENT_TYPE_PPT.equals(dbFileInfoDto.getContentType()) || CoreConstant.CONTENT_TYPE_PPTX.equals(dbFileInfoDto.getContentType())) {
                // PPT
                dbFileInfoDto.setViewLink(dbFileProperties.getOfficeOnlineHost() + "/p/PowerPointFrame.aspx?PowerPointView=ReadingView&WOPISrc=" + dbFileProperties.getOfficeOnlineWopiSrcHost() + "/wopi/files/" + dbFileInfoDto.getId());
                dbFileInfoDto.setEmbedViewLink(dbFileProperties.getOfficeOnlineHost() + "/p/PowerPointFrame.aspx?PowerPointView=ChromelessView&Embed=1&WOPISrc=" + dbFileProperties.getOfficeOnlineWopiSrcHost() + "/wopi/files/" + dbFileInfoDto.getId());
            } else if (CoreConstant.CONTENT_TYPE_PDF.equals(dbFileInfoDto.getContentType())) {
                //PDF
                dbFileInfoDto.setViewLink(dbFileProperties.getOfficeOnlineHost() + "/wv/wordviewerframe.aspx?PdfMode=1&WOPISrc=" + dbFileProperties.getOfficeOnlineWopiSrcHost() + "/wopi/files/" + dbFileInfoDto.getId());
                dbFileInfoDto.setEmbedViewLink(dbFileProperties.getOfficeOnlineHost() + "/wv/wordviewerframe.aspx?embed=1&PdfMode=1&WOPISrc=" + dbFileProperties.getOfficeOnlineWopiSrcHost() + "/wopi/files/" + dbFileInfoDto.getId());
            }
        }
    }

    /**
     * 获得文件物理路径
     *
     * @param id
     * @return 文件物理路径
     */
    private String getPhysicalPath(String id) {
        String physicalPath = null;
        DbFileInfoEntity dbFileInfoEntity = this.getEntity(id);
        if (dbFileInfoEntity != null) {
            physicalPath = new StringBuilder()
                    .append(dbFileProperties.getMapping().get(dbFileInfoEntity.getRootPathCode()))
                    .append(dbFileInfoEntity.getPhysicalRelativePath())
                    .append(dbFileInfoEntity.getPhysicalFileName())
                    .toString();
        }
        return physicalPath;
    }

    /**
     * 获得文件实体
     *
     * @param id id
     * @return DbFileInfoEntity
     */
    private DbFileInfoEntity getEntity(String id) {
        return iDbFileInfoRepository.selectOne(new QueryWrapper<DbFileInfoEntity>().lambda()
                .eq(DbFileInfoEntity::getId, id)
                .eq(DbFileInfoEntity::getApplicationName, applicationName)
                .eq(DbFileInfoEntity::getProfilesActive, profilesActive)
        );
    }


}
