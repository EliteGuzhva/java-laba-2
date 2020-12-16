package com.eliteguzhva;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IniParser {
    final Pattern _section  = Pattern.compile( "\\s*\\[([^]]*)]\\s*" );
    final Pattern _keyValue = Pattern.compile( "\\s*([^=]*)=(.*)" );

    final Map<String, Map<String, String>>  _entries  = new HashMap<>();
    final ByteArrayOutputStream _iniContent = new ByteArrayOutputStream();

    public boolean read(String filename) {
        File iniFile = new File(filename);

        try {
            readFile(iniFile);
            load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public void printContent() {
        for (String section : _entries.keySet()) {
            System.out.println("Section: " + section);
            _entries.get(section).forEach((k, v) -> System.out.println(k + " = " + v));
            System.out.println();
        }
    }

    private void readFile(File iniFile) throws Exception
    {
        FileInputStream fin = new FileInputStream(iniFile);
        int n;
        byte[] buf = new byte[4096];
        while ((n = fin.read(buf)) > -1)
            _iniContent.write(buf, 0, n);
    }

    private void load() throws Exception
    {
        if (_iniContent.size() == 0)
            throw new IOException("Ini-file content is empty!");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(_iniContent.toByteArray());
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        try(BufferedReader br = new BufferedReader(streamReader))
        {
            String line;
            String section = null;
            while ((line = br.readLine()) != null)
            {
                Matcher matcher = _section.matcher(line);
                if (matcher.matches())
                    section = matcher.group(1).trim();
                else if (section != null)
                {
                    matcher = _keyValue.matcher(line);
                    if(matcher.matches())
                    {
                        String key = matcher.group(1).trim();
                        String value = matcher.group(2).trim();
                        Map<String, String> kv = _entries.computeIfAbsent(section, k -> new HashMap<>());
                        kv.put(key, value);
                    }
                }
            }
        }
    }
}
