package jjoommnn.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
public class Index
{
    @RequestMapping("/index.do")
    public String index()
    {
        return "index";
    }
    
    @RequestMapping("/pdfdownload.do")
    public void pdfDownload( HttpServletRequest request, HttpServletResponse response )
    {
        response.setContentType( "application/pdf" );
        
        OutputStream rOut = null;
        Document document = new Document();
        try
        {
            rOut = response.getOutputStream();
            
            PdfWriter.getInstance( document, rOut );
            document.open();
            
            String fontUrl = this.getClass().getClassLoader().getResource( "jjoommnn/fonts/NanumGothic.ttf" ).toString();
            
            BaseFont bfComic = BaseFont.createFont( fontUrl, BaseFont.IDENTITY_H, BaseFont.EMBEDDED );
            com.itextpdf.text.Font font = new com.itextpdf.text.Font( bfComic, 12 );
            
            document.add( new Paragraph( "-- 바 차트 시작--", font ) );
            
            CategoryDataset data = createDataset();
            JFreeChart chart = createChart( data );
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsJPEG( bOut, chart, 400, 300 );
            
            Image jpg = Image.getInstance( bOut.toByteArray() );
            document.add( jpg );
            
            document.add( new Paragraph( "-- 바 차트 끝 --", font ) );
        }
        catch( DocumentException de )
        {
            System.err.println( de.getMessage() );
        }
        catch( IOException ioe )
        {
            System.err.println( ioe.getMessage() );
        }
        finally
        {
            document.close();
            if( rOut != null ) try { rOut.close(); } catch( Exception e ) {}
        }
    }
    
    //http://www.java2s.com/Code/Java/Chart/CatalogChart.htm
    
    private static CategoryDataset createDataset()
    {
        // row keys...
        final String series1 = "한글";
        final String series2 = "Second";
        final String series3 = "Third";

        // column keys...
        final String category1 = "한글 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";
        final String category5 = "Category 5";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue( 1.0, series1, category1 );
        dataset.addValue( 4.0, series1, category2 );
        dataset.addValue( 3.0, series1, category3 );
        dataset.addValue( 5.0, series1, category4 );
        dataset.addValue( 5.0, series1, category5 );

        dataset.addValue( 5.0, series2, category1 );
        dataset.addValue( 7.0, series2, category2 );
        dataset.addValue( 6.0, series2, category3 );
        dataset.addValue( 8.0, series2, category4 );
        dataset.addValue( 4.0, series2, category5 );

        dataset.addValue( 4.0, series3, category1 );
        dataset.addValue( 3.0, series3, category2 );
        dataset.addValue( 2.0, series3, category3 );
        dataset.addValue( 3.0, series3, category4 );
        dataset.addValue( 6.0, series3, category5 );

        return dataset;
    }
    
    private static JFreeChart createChart( final CategoryDataset dataset )
    {
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart( "Bar Chart 한글", // chart
                                                                              // title
                "Category", // domain axis label
                "Value", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
                );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint( Color.white );

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        // plot.setBackgroundPaint( Color.lightGray );
        plot.setDomainGridlinePaint( Color.black );
        plot.setRangeGridlinePaint( Color.black );
        
        InputStream fontIn = Index.class.getClassLoader().getResourceAsStream( "jjoommnn/fonts/NanumGothic.ttf" );

        Font font = plot.getDomainAxis().getLabelFont();
        Font newFont = null;
        
        try
        {
            newFont = Font.createFont( Font.TRUETYPE_FONT, fontIn );
        }
        catch(Exception e )
        {
            e.printStackTrace();
            throw new RuntimeException( e );
        }
        
        // X축 라벨
        plot.getDomainAxis().setLabelFont( newFont );
        // X축 도메인
        plot.getDomainAxis().setTickLabelFont( newFont );

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        rangeAxis.setStandardTickUnits( NumberAxis.createIntegerTickUnits() );

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer)plot.getRenderer();
        renderer.setDrawBarOutline( false );

        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint( 0.0f, 0.0f, Color.blue, 0.0f, 0.0f, Color.blue );
        final GradientPaint gp1 = new GradientPaint( 0.0f, 0.0f, Color.green, 0.0f, 0.0f, Color.green );
        final GradientPaint gp2 = new GradientPaint( 0.0f, 0.0f, Color.red, 0.0f, 0.0f, Color.red );
        renderer.setSeriesPaint( 0, gp0 );
        renderer.setSeriesPaint( 1, gp1 );
        renderer.setSeriesPaint( 2, gp2 );

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions( CategoryLabelPositions.createUpRotationLabelPositions( Math.PI / 6.0 ) );
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;
    }
}
