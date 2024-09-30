from sqlalchemy import create_engine,text
import pandas as pd

class DataVizOpr:
    def __init__(self, host='localhost', port=1972, namespace='USER', username='SuperUser', password='SYS') -> None:          
        self.CONNECTION_STRING = f"iris://{username}:{password}@{host}:{port}/{namespace}"
        self.engine = create_engine(self.CONNECTION_STRING)
        #self.conn = engine.connect()
        
    def get_df(self,tbl,where):  
        #tbl = "VisEDA.BikeSharing"      
        with self.engine.connect() as conn:
            with conn.begin():# Load 
                if len(where) == 0:
                    sql = f"""
                        SELECT  * from {tbl}
                    """
                else:
                    sql = f"""
                        SELECT * from {tbl} where {where}
                    """           
                result = conn.execute(text(sql))
                result_df = pd.DataFrame(result)
                if len(result_df) == 0:
                    return 0
                else:                                  
                    return result_df

    def get_schema(self):   
            
        with self.engine.connect() as conn:
            with conn.begin():     
                sql = text(""" 
                    SELECT distinct TABLE_SCHEMA
                    FROM INFORMATION_SCHEMA.TABLES                   
                    WHERE TABLE_TYPE='BASE TABLE'       
                    order by TABLE_SCHEMA
                    """)
                results = []
                try:
                    results = conn.execute(sql).fetchall()
                    schemas="0"                    
                    for element in results:                                 
                       if schemas == "0":
                           schemas = element[0]
                       else:
                           schemas = schemas+","+element[0]
                except Exception as e:
                    print(e)
                    
        return schemas

    def get_tables(self,schema):   
            
        with self.engine.connect() as conn:
            with conn.begin():     
                sql = text(""" 
                    SELECT TABLE_NAME
                    FROM INFORMATION_SCHEMA.TABLES                   
                    WHERE TABLE_TYPE='BASE TABLE'
                    AND TABLE_SCHEMA = :schema
                     order by TABLE_NAME
                    """).bindparams(schema=schema)            
                results = []
                try:
                    results = conn.execute(sql).fetchall()                  
                    tables="0"                    
                    for element in results:                                 
                       if tables == "0":
                           tables = element[0]
                       else:
                           tables = tables+","+element[0]
                except Exception as e:
                    print(e)
                    
        return tables
    
    def get_row_count(self,tbl):               
        with self.engine.connect() as conn:
            with conn.begin():     
                sql = text(f"""
                    SELECT top 1 * from {tbl}
                    """)
              
                results = []
                try:
                    results = conn.execute(sql).fetchall()
                  
                except Exception as e:
                    print(e)
                    
        return len(results)
   
