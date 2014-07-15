package jjoommnn.test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TestPdf
{
    public static void main( String[] args )
    {
        PDDocument doc = null;
        PDPage page = null;
        try
        {
            doc = new PDDocument();
            page = new PDPage();
            doc.addPage( page );
            
            PDFont font = PDType1Font.HELVETICA_BOLD;

            PDPageContentStream content = new PDPageContentStream( doc, page );
            content.beginText();
            content.setFont( font, 12 );
            content.moveTextPositionByAmount( 100, 700 );
            content.drawString( "Hello from www.printmyfolders.com" );
            content.endText();
            content.close();
            
            doc.save( "C:\\Temp\\PDFWithText.pdf" );
            doc.close();
        }
        catch( Exception e )
        {
            System.out.println( e );
        }
    }
}
