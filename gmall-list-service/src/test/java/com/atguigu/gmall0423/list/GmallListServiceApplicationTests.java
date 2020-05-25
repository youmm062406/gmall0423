package com.atguigu.gmall0423.list;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallListServiceApplicationTests {

    @Autowired
    JestClient jestClient;
    @Test
    public void contextLoads() {
    }

    /**
     * 测试能否与es连通
     * 1.定义dsl语句
     * 2.定义执行的动作
     * 3.执行动作
     * 4.获取执行之后的结果集
     */
//    @Test
//    public void testES() throws IOException {
//        //GET /movie_chn/movie/_search
//        String queryDsl="{\n" +
//                "  \"query\": {\n" +
//                "    \"term\": {\n" +
//                "      \"actorList.name\": \"张译\"\n" +
//                "    }\n" +
//                "  }\n" +
//                "}";
//        //查询get
//        Search build = new Search.Builder(queryDsl).addIndex("movie_chn").addType("movie").build();
//        //执行
//        SearchResult searchResult = jestClient.execute(build);
//        //获取数据
//        List<SearchResult.Hit<Map, Void>> hits = searchResult.getHits(Map.class);
//        for(SearchResult.Hit<Map, Void> hit:hits){
//            Map map = hit.source;
//            System.out.println(map.get("name"));
//        }
//    }
}
