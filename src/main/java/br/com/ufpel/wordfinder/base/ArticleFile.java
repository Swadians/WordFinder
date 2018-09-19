/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.base;

import java.util.Objects;

/**
 *
 * @author WeslenSchiavon
 */
public class ArticleFile implements Comparable<ArticleFile> {

    public String fileName;
    public String folder;

    public ArticleFile(String fileName, String folder) {
        this.fileName = fileName;
        this.folder = folder;
    }

    @Override
    public int compareTo(ArticleFile o) {
        return this.fileName.compareTo(o.fileName);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.fileName);
        hash = 29 * hash + Objects.hashCode(this.folder);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArticleFile other = (ArticleFile) obj;
        if (!Objects.equals(this.fileName, other.fileName)) {
            return false;
        }
        if (!Objects.equals(this.folder, other.folder)) {
            return false;
        }
        return true;
    }

}
