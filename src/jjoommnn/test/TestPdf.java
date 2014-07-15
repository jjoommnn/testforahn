package jjoommnn.test;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class TestPdf
{
    public static void main( String[] args )
    {
        System.out.println( "True Types (embedded)" );

        // step 1: creation of a document-object
        Document document = new Document();

        try
        {
            // step 2: creation of the writer-object
            PdfWriter.getInstance( document, new FileOutputStream( "C:\\Temp\\unicode.pdf" ) );

            // step 3: we open the document
            document.open();

            // step 4: we add content to the document
            BaseFont bfComic = BaseFont.createFont( "c:\\windows\\fonts\\malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED );
            Font font = new Font( bfComic, 12 );
            String text1 = "한글 This is the quite popular True Type font 'Comic'.";
            String text2 = "Some greek characters: \u0393\u0394\u03b6";
            String text3 = "Some cyrillic characters: \u0418\u044f";
            document.add( new Paragraph( text1, font ) );
            document.add( new Paragraph( text2, font ) );
            document.add( new Paragraph( text3, font ) );
        }
        catch( DocumentException de )
        {
            System.err.println( de.getMessage() );
        }
        catch( IOException ioe )
        {
            System.err.println( ioe.getMessage() );
        }

        // step 5: we close the document
        document.close();

    }
}
