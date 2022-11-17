package org.tiny.keycloak;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * プロパティファイルを読み込んで値を供給するクラス.
 *
 * @author daianjimax
 */
public class PropertyReader {

    private Properties properties = null;

    /**
     * コンストラクタ
     *
     * @param filePath プロパティファイルのパス
     */
    public PropertyReader(String filePath) {
        File f = new File(filePath);
        Logger.getLogger(this.getClass().getName()).info(f.getAbsolutePath());
        try (BufferedReader br = Files.newBufferedReader(Path.of(f.getAbsolutePath()), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(br);
        } catch (IOException ex) {
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * プロパティを取得する
     *
     * @param key 検索キー
     * @return プロパティ値
     */
    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }

}
