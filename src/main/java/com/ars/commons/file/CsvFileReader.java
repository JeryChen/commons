package com.ars.commons.file;

import org.apache.commons.lang.StringUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话介绍功能〉<br>
 * csv文件读取
 *
 * @author jierui on 2020-01-08.
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class CsvFileReader {

    private String columnDelimiter;

    private BufferedRandomAccessFile reader;

    private long nextPos;

    public CsvFileReader(String filePath, String columnDelimiter) throws IOException {
        this(new File(filePath), columnDelimiter);
    }

    public CsvFileReader(File file, String columnDelimiter) throws IOException {
        this.columnDelimiter = columnDelimiter;
        this.reader = new BufferedRandomAccessFile(file, "r");
        this.nextPos = 0;
    }

    /**
     * 读取标题
     *
     * @return 标题数据
     */
    public List<String[]> readHeader() {
        return readLine(0, 1);
    }

    /**
     * 读取数据
     *
     * @param limit 读取行数
     * @return      数据列表
     */
    public List<String[]> readData(int limit) {
        return readLine(nextPos, limit);
    }

    /**
     * 使用BufferedRandomAccessFile读取文件
     *
     * @param pos    偏移量
     * @param limit  读取行数
     * @return  数据列表
     */
    public List<String[]> readLine(long pos, int limit) {
        List<String[]> dataList = new ArrayList();
        try {
            reader.seek(pos);
            for (int i = 0; i < limit; i++) {
                String pin = reader.readLine();
                if (StringUtils.isBlank(pin)) {
                    break;
                }
                dataList.add(pin.split(columnDelimiter));
            }
            nextPos = reader.getFilePointer();
            return dataList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("文件解析异常", e);
        }
    }

    public void close(){
        closeQuietly(reader);
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
