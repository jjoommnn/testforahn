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
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
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
            
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsJPEG( bOut, barChart(), 400, 300 );
            
            Image jpg = Image.getInstance( bOut.toByteArray() );
            document.add( jpg );
            
            document.add( new Paragraph( "-- 바 차트 끝 --", font ) );
            
            bOut = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsJPEG( bOut, barLineChart(), 400, 300 );
            jpg = Image.getInstance( bOut.toByteArray() );
            document.add( jpg );
            
            bOut = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsJPEG( bOut, pieChart(), 400, 300 );
            jpg = Image.getInstance( bOut.toByteArray() );
            document.add( jpg );
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
    
    private static JFreeChart barChart()
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
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart( "Bar Chart 한글", // chart title
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
    
    private static JFreeChart barLineChart()
    {
        // create the first dataset...
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        dataset1.addValue(1.0, "S1", "Category 1");
        dataset1.addValue(4.0, "S1", "Category 2");
        dataset1.addValue(3.0, "S1", "Category 3");
        dataset1.addValue(5.0, "S1", "Category 4");
        dataset1.addValue(5.0, "S1", "Category 5");
        dataset1.addValue(7.0, "S1", "Category 6");
        dataset1.addValue(7.0, "S1", "Category 7");
        dataset1.addValue(8.0, "S1", "Category 8");

        dataset1.addValue(5.0, "S2", "Category 1");
        dataset1.addValue(7.0, "S2", "Category 2");
        dataset1.addValue(6.0, "S2", "Category 3");
        dataset1.addValue(8.0, "S2", "Category 4");
        dataset1.addValue(4.0, "S2", "Category 5");
        dataset1.addValue(4.0, "S2", "Category 6");
        dataset1.addValue(2.0, "S2", "Category 7");
        dataset1.addValue(1.0, "S2", "Category 8");

        // create the first renderer...
        //      final CategoryLabelGenerator generator = new StandardCategoryLabelGenerator();
        final CategoryItemRenderer renderer = new BarRenderer();
        //    renderer.setLabelGenerator(generator);
        renderer.setItemLabelsVisible(true);
        
        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);
        
        plot.setDomainAxis(new CategoryAxis("Category"));
        plot.setRangeAxis(new NumberAxis("Value"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        // now create the second dataset and renderer...
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        dataset2.addValue(9.0, "T1", "Category 1");
        dataset2.addValue(7.0, "T1", "Category 2");
        dataset2.addValue(2.0, "T1", "Category 3");
        dataset2.addValue(6.0, "T1", "Category 4");
        dataset2.addValue(6.0, "T1", "Category 5");
        dataset2.addValue(9.0, "T1", "Category 6");
        dataset2.addValue(5.0, "T1", "Category 7");
        dataset2.addValue(4.0, "T1", "Category 8");

        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);

        // create the third dataset and renderer...
        final ValueAxis rangeAxis2 = new NumberAxis("Axis 2");
        plot.setRangeAxis(1, rangeAxis2);

        DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();
        dataset3.addValue(94.0, "R1", "Category 1");
        dataset3.addValue(75.0, "R1", "Category 2");
        dataset3.addValue(22.0, "R1", "Category 3");
        dataset3.addValue(74.0, "R1", "Category 4");
        dataset3.addValue(83.0, "R1", "Category 5");
        dataset3.addValue(9.0, "R1", "Category 6");
        dataset3.addValue(23.0, "R1", "Category 7");
        dataset3.addValue(98.0, "R1", "Category 8");

        plot.setDataset(2, dataset3);
        final CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
        plot.setRenderer(2, renderer3);
        plot.mapDatasetToRangeAxis(2, 1);

        // change the rendering order so the primary dataset appears "behind" the 
        // other datasets...
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("Overlaid Bar Chart");
        
        return chart;
    }
    
    private static JFreeChart pieChart()
    {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("One", new Double(43.2));
        dataset.setValue("Two", new Double(10.0));
        dataset.setValue("Three", new Double(27.5));
        dataset.setValue("Four", new Double(17.5));
        dataset.setValue("Five", new Double(11.0));
        dataset.setValue("Six", new Double(19.4));
        
        JFreeChart chart = ChartFactory.createPieChart(
                "Pie Chart Demo 1",  // chart title
                dataset,             // data
                true,               // include legend
                true,
                false
            );

            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setNoDataMessage("No data available");
            plot.setCircular(false);
            plot.setLabelGap(0.02);
            
        return chart;
    }
}
