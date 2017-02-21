# CodeGeneration
on giving a json this project will develop all the respective pojo, dao


sample input
{
  "location": "F:/",
  "name": "Company",
  "createPojo": "true",
  "pk": "id",
  "pkType": "String",
  "lowerCaseName": "true",
  "lowerCaseFieldName": "true",
  "fields": [
    {
      "name": "id",
      "type": "String"
    },
    {
      "name": "name",
      "type": "String"
    },
    {
      "name": "ein",
      "type": "String"
    },
    {
      "name": "type",
      "type": "String"
    },
    {
      "name": "phone_number",
      "type": "String"
    },
    {
      "name": "address",
      "type": "String"
    },
    {
      "name": "city",
      "type": "String"
    },
    {
      "name": "state",
      "type": "String"
    },
    {
      "name": "country",
      "type": "String"
    },
    {
      "name": "zipcode",
      "type": "String"
    },
    {
      "name": "currency",
      "type": "String"
    },
    {
      "name": "email",
      "type": "String"
    },
    {
      "name": "payment_info",
      "type": "String"
    },
    {
      "name": "createdBy",
      "type": "String"
    },
    {
      "name": "modifiedBy",
      "type": "String"
    },
    {
      "name": "createdDate",
      "type": "String"
    },
    {
      "name": "modifiedDate",
      "type": "String"
    },
    {
      "name": "owner",
      "type": "String"
    },
    {
      "name": "active",
      "type": "boolean"
    }
  ]
}


query used to get columns
SELECT GROUP_CONCAT(`COLUMN_NAME`)
FROM `INFORMATION_SCHEMA`.`COLUMNS` 
WHERE `TABLE_SCHEMA`='qount' 
    AND `TABLE_NAME`='company';
