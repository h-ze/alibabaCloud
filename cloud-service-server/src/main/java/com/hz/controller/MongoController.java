package com.hz.controller;


import com.common.entity.ResponseResult;
import com.hz.entity.MongoEntity;
import com.hz.mongo.MongoService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mongo")
@Api(tags = "Mongo接口")
public class MongoController {

    @Autowired
    private MongoService mongoService;

    @GetMapping("list")
    @ApiOperation(notes = "获取mongo列表",value = "获取mongo列表")
    @ApiImplicitParam(name = "id",value = "集合名称",dataType = "Long",paramType = "query")
    public ResponseResult getMongoList(@RequestParam("id")Long id){
        MongoEntity mongoEntity = (MongoEntity) mongoService.selectById(id, MongoEntity.class, "hz");
        return ResponseResult.successResult(100000,mongoEntity);
    }

    @PostMapping("addMongo")
    @ApiOperation(notes = "插入数据",value = "往mongo中指定的集合中插入一条数据")
    public ResponseResult addMongo(@RequestBody @ApiParam(name = "body",value = "添加一条mongo信息",required = true) MongoEntity mongoEntity){
        mongoService.insert(mongoEntity,"hz");
        return ResponseResult.successResult(100000,"插入成功");
    }

    @DeleteMapping("deleteMongo")
    @ApiOperation(notes = "删除一条数据",value ="删除一条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id名称",dataType = "Long",paramType = "query")

    })
    public ResponseResult deleteMongo(@RequestParam("id")Long id){
        mongoService.deleteById(id,MongoEntity.class,"hz");
        return ResponseResult.successResult(100000,"删除成功");
    }

    @PutMapping("updateMongo")
    @ApiOperation(notes = "更新一条数据",value ="更新一条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id名称",dataType = "Long",paramType = "query")

    })
    public ResponseResult updateMongo(@RequestParam("id")Long id,@RequestBody @ApiParam(name = "body",value = "添加一条mongo信息",required = true) MongoEntity mongoEntity){
        mongoService.updateById(id,"hz",mongoEntity);
        return ResponseResult.successResult(100000,"更新成功");
    }


    @PostMapping("index")
    @ApiOperation(notes = "设置索引",value ="设置索引")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "collectionName",value = "集合名",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "indexName",value = "索引名称",dataType = "String",paramType = "query")

    })
    public ResponseResult insertIndex(@RequestParam("collectionName")String collectionName,@RequestParam("indexName")String indexName){
        String docId = mongoService.createIndex(collectionName, indexName);
        return ResponseResult.successResult(100000,"设置成功");
    }

    @DeleteMapping("index")
    @ApiOperation(notes = "删除索引",value ="删除索引")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "collectionName",value = "集合名",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "indexName",value = "索引名称",dataType = "String",paramType = "query")
    })
    public ResponseResult deleteIndex(@RequestParam("collectionName")String collectionName,@RequestParam("indexName")String indexName){
        String docId = mongoService.dropIndex(collectionName, indexName);
        return ResponseResult.successResult(100000,"删除成功");
    }

    @GetMapping("index")
    @ApiOperation(notes = "获取所有索引",value ="获取所有索引")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "collectionName",value = "集合名",dataType = "String",paramType = "query")

    })
    public ResponseResult getIndex(@RequestParam("collectionName")String collectionName){
        List allIndexes = mongoService.getAllIndexes(collectionName);
        return ResponseResult.successResult(100000,allIndexes);
    }

}
