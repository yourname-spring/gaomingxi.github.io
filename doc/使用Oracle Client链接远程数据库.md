## 使用Oracle Client链接远程数据库

#### 配置两个环境变量

*     TNS目录环境变量：
    *     TNS_ADMIN：TNS文件所在目录
*     指定字符集字符集：
    *     NLS_LANG：SIMPLIFIED CHINESE_CHINA.ZHS16GBK





#### 修改PL SQL配置，指定OCI.DLL运行库

*    Tools”--“Preferences”-- “Connection”中的Oracle Home处填写oci.dll文件夹的地址，在OCI library中填写oci.dll文件的地址，然后“Apply”--“OK”，重启plsql 检查该配置是否生效，如果未生效再配置一次