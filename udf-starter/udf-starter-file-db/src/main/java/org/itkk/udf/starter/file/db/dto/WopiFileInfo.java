package org.itkk.udf.starter.file.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class WopiFileInfo implements Serializable {
    private static final long serialVersionUID = -2285870957950117030L;
    // 必需的响应属性
    @JsonProperty("BaseFileName")
    private String baseFileName; //文件的字符串名称，包括扩展名，不带路径。 用于在用户界面（UI）中显示，并确定文件的扩展名。
    @JsonProperty("OwnerId")
    private String ownerId; //一个唯一标识文件所有者的字符串。 在大多数情况下，上载或创建文件的用户应被视为所有者。
    @JsonProperty("Size")
    private Long size; //文件大小，以字节为单位，表示为一个长的64位带符号整数。
    @JsonProperty("UserId")
    private String userId; //一个字符串值，用于唯一标识当前正在访问文件的用户。
    @JsonProperty("Version")
    private Long version; //基于服务器的文件版本架构的文件的当前版本，以字符串形式。 文件更改时，此值必须更改，并且版本值绝不能重复给定文件。
    // WOPI主机功能属性
    //@JsonProperty("SupportedShareUrlTypes")
    //private List<String> supportedShareUrlTypes; //包含主机支持的共享URL类型的字符串数组 可能的值:ReadOnly,ReadWrite
    //@JsonProperty("SupportedShareUrlTypes")
    //private Boolean supportsCobalt; //(oneNote专用)一个布尔值，指示主机支持以下WOPI操作 操作 : ExecuteCellStorageRequest,ExecuteCellStorageRelativeRequest
    //@JsonProperty("SupportsContainers")
    //private Boolean supportsContainers; //一个布尔值，指示主机支持以下WOPI操作 操作 : CheckContainerInfo,CreateChildContainer,CreateChildFile,DeleteContainer,DeleteFile,EnumerateAncestors (containers),EnumerateAncestors (files),EnumerateChildren (containers),GetEcosystem (containers),RenameContainer
    //@JsonProperty("SupportsDeleteFile")
    //private Boolean supportsDeleteFile; //一个布尔值，指示主机支持DeleteFile操作。
    //@JsonProperty("SupportsEcosystem")
    //private Boolean supportsEcosystem; //一个布尔值，指示主机支持以下WOPI操作 : CheckEcosystem,GetEcosystem (containers),GetEcosystem (files),GetRootContainer (ecosystem)
    //@JsonProperty("SupportsExtendedLockLength")
    //private Boolean supportsExtendedLockLength; //一个布尔值，指示主机支持最长为1024个ASCII字符的锁ID。 如果未提供，WOPI客户端将假定锁ID限制为256个ASCII字符。
    //@JsonProperty("SupportsFolders")
    //private Boolean supportsFolders; //一个布尔值，指示主机支持以下WOPI操作 : CheckFolderInfo,EnumerateChildren (folders),DeleteFile
    //@JsonProperty("SupportsGetFileWopiSrc")
    //private Boolean supportsGetFileWopiSrc; //一个布尔值，指示主机支持🚧GetFileWopiSrc（生态系统）操作。
    //@JsonProperty("SupportsGetLock")
    //private Boolean supportsGetLock; //一个布尔值，指示主机支持GetLock操作。
    @JsonProperty("SupportsLocks")
    private Boolean supportsLocks; //一个布尔值，指示主机支持以下WOPI操作：Lock,Unlock,RefreshLock,UnlockAndRelock operations for this file.
    //@JsonProperty("SupportsRename")
    //private Boolean supportsRename; //一个布尔值，指示主机支持RenameFile操作。
    @JsonProperty("SupportsUpdate")
    private Boolean supportsUpdate; //一个布尔值，指示主机支持以下WOPI操作：PutFile,PutRelativeFile
    //@JsonProperty("SupportsUserInfo")
    //private Boolean supportsUserInfo; //一个布尔值，指示主机支持PutUserInfo操作。
    // 用户元数据属性
    //@JsonProperty("IsAnonymousUser")
    //private Boolean isAnonymousUser; //一个布尔值，指示用户是否通过主机验证
    //@JsonProperty("IsEduUser")
    //private Boolean isEduUser; //指示用户是否为教育用户的布尔值。
    //@JsonProperty("LicenseCheckForEditIsEnabled")
    //private Boolean licenseCheckForEditIsEnabled; //一个布尔值，指示用户是否是企业用户。
    //@JsonProperty("UserFriendlyName")
    //private String userFriendlyName; //是用户名的字符串，适合在UI中显示。
    //@JsonProperty("UserInfo")
    //private String userInfo; //包含有关用户信息的字符串值。
    // 用户权限属性
    //@JsonProperty("ReadOnly")
    //private Boolean readOnly; //一个布尔值，指示对此用户无法更改文件。
    //@JsonProperty("RestrictedWebViewOnly")
    //private Boolean restrictedWebViewOnly; //一个布尔值，指示WOPI客户端应限制用户可以对文件执行的操作。 此属性的行为取决于WOPI客户端。
    //@JsonProperty("UserCanAttend")
    //private Boolean userCanAttend; //一个布尔值，指示用户有权查看此文件的广播。
    @JsonProperty("UserCanNotWriteRelative")
    private Boolean userCanNotWriteRelative; //一个布尔值，指示用户没有足够的权限在WOPI服务器上创建新文件。 将此设置为true会告诉WOPI客户端，此用户在当前文件上对PutRelativeFile的调用将失败。
    //@JsonProperty("UserCanPresent")
    //private Boolean userCanPresent; //一个布尔值，指示用户有权向有权广播或查看当前文件的广播的一组用户广播此文件。
    //@JsonProperty("UserCanRename")
    //private Boolean userCanRename; //一个布尔值，指示用户有权重命名当前文件。
    @JsonProperty("UserCanWrite")
    private Boolean userCanWrite; //一个布尔值，指示用户有权更改文件。 将其设置为true会告诉WOPI客户端它可以代表用户调用PutFile。
    // 文件URL属性
    //@JsonProperty("CloseUrl")
    //private String closeUrl; //当应用程序关闭时，或者在发生不可恢复的错误时，WOPI客户端应导航到的网页的URI。
    //@JsonProperty("DownloadUrl")
    //private String downloadUrl; //该文件的用户可访问URI，旨在允许用户下载该文件的副本。
    //@JsonProperty("FileEmbedCommandUrl")
    //private String fileEmbedCommandUrl; //允许用户向文件创建可嵌入URI的位置的URI。
    //@JsonProperty("FileSharingUrl")
    //private String fileSharingUrl; //允许用户共享文件的位置的URI。
    //@JsonProperty("FileUrl")
    //private String fileUrl; //WOPI客户端用来获取文件的文件位置的URI。
    //@JsonProperty("FileVersionUrl")
    //private String fileVersionUrl; //允许用户查看文件版本历史记录的位置的URI。
    //@JsonProperty("HostEditUrl")
    //private String hostEditUrl; //加载编辑WOPI操作的主机页面的URI。
    //@JsonProperty("HostEmbeddedViewUrl")
    //private String hostEmbeddedViewUrl; //网页的URI，可访问可嵌入另一个HTML页面的文件的查看体验。 这通常是指向加载embedview WOPI操作的主机页面的URI。
    //@JsonProperty("HostViewUrl")
    //private String hostViewUrl; //加载视图WOPI操作的主机页面的URI。 Office Online使用此URL在视图和编辑模式之间导航。
    //@JsonProperty("SignoutUrl")
    //private String signoutUrl; //用来将当前用户从主机身份验证系统中签名的URI。
    // 面包屑属性
    //@JsonProperty("BreadcrumbBrandName")
    //private String breadcrumbBrandName; //指示主机品牌名称的字符串。
    //@JsonProperty("BreadcrumbBrandUrl")
    //private String breadcrumbBrandUrl; //用户单击显示BreadcrumbBrandName的UI时，WOPI客户端应导航到的网页的URI。
    //@JsonProperty("BreadcrumbDocName")
    //private String breadcrumbDocName; //指示文件名的字符串。 如果未提供，则WOPI客户端可以使用BaseFileName值。
    //@JsonProperty("BreadcrumbFolderName")
    //private String breadcrumbFolderName; //一个字符串，指示包含文件的容器的名称。
    //@JsonProperty("BreadcrumbFolderUrl")
    //private String BreadcrumbFolderUrl; //当用户单击显示BreadcrumbFolderName的UI时，WOPI客户端应导航到的网页的URI。
    // 其他杂项
    //@JsonProperty("AllowAdditionalMicrosoftServices")
    //private Boolean allowAdditionalMicrosoftServices; //指示WOPI客户端可以连接到Microsoft服务以提供最终用户功能的布尔值。
    //@JsonProperty("AllowErrorReportPrompt")
    //private Boolean allowErrorReportPrompt; //一个布尔值，指示在发生错误的情况下，允许WOPI客户端提示用户允许收集有关其特定错误的详细报告。 收集的信息可能包括用户的文件和其他特定于会话的状态。
    @JsonProperty("AllowExternalMarketplace")
    private Boolean allowExternalMarketplace; //指示WOPI客户端的布尔值可以允许连接到文件中引用的外部服务（例如，可嵌入JavaScript应用程序的市场）。
    //@JsonProperty("CloseButtonClosesWindow")
    //private Boolean closeButtonClosesWindow; //一个布尔值，指示当用户激活WOPI客户端中的任何“关闭UI”时，WOPI客户端应关闭窗口或选项卡。
    //@JsonProperty("CopyPasteRestrictions")
    //private String copyPasteRestrictions; //一个字符串值，指示WOPI客户端是否应在应用程序内禁用复制和粘贴功能。 默认设置是允许所有复制和粘贴功能，即该设置无效。 可能的值 : BlockAll,CurrentDocumentOnly
    @JsonProperty("DisablePrint")
    private Boolean disablePrint; //指示WOPI客户端应禁用所有打印功能的布尔值。
    @JsonProperty("DisableTranslation")
    private Boolean disableTranslation; //指示WOPI客户端应禁用所有机器翻译功能的布尔值。
    //@JsonProperty("FileExtension")
    //private String fileExtension; //一个字符串值，表示文件的文件扩展名。 该值必须以..开头。如果提供，WOPI客户端将使用此值作为文件扩展名。 否则，将从BaseFileName解析扩展名。
    //@JsonProperty("FileNameMaxLength")
    //private Integer fileNameMaxLength; //一个整数值，指示WOPI主机支持的文件名的最大长度（不包括文件扩展名）。 默认值为250。请注意，如果省略了该属性或将其显式设置为0，则WOPI客户端将使用此默认值。
    //@JsonProperty("LastModifiedTime")
    //private String lastModifiedTime; //一个字符串，代表上次修改文件的时间。 此时间必须始终是UTC时间，并且必须采用ISO 8601往返格式。 例如，“ 2009-06-15T13：45：30.0000000Z”。
    @JsonProperty("Sha256")
    private String sha256; //文件内容的256位SHA-2-编码的[FIPS 180-2]哈希，作为Base64编码的字符串。 用于WOPI客户端中的缓存目的。
    //@JsonProperty("UniqueContentId")
    //private String uniqueContentId; //在特殊情况下，主机可能会选择不提供SHA256，但仍具有某种机制来识别两个不同的文件包含与使用SHA256相同的内容的相同内容。 仅当主机可以保证具有相同内容的两个不同文件具有相同的UniqueContentId值时，才可以提供此字符串值而不是SHA256值。
}
