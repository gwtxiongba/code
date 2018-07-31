/*
*
*      Author ：zhy
*        Date : 2014-10-31
* Description : 下拉树
* 
*/
Ext.define('Ext.ux.TreeCombo', {
    extend: 'Ext.form.field.Picker',
    requires: ['Ext.tree.Panel'],
    alternateClassName: 'Ext.ux.TreeCombo',
    alias: ['widget.treecombo'],
    triggerCls: Ext.baseCSSPrefix + 'form-arrow-trigger',

    /**
     * @private
     * @cfg {String}
     * CSS class used to find the {@link #hiddenDataEl}
     */
    hiddenDataCls: Ext.baseCSSPrefix + 'hide-display ' + Ext.baseCSSPrefix + 'form-data-hidden',
    valueField: "id",
    displayField: "text",
    preloading: false,
    root: 'children',
    url: '',
    delimiter: ', ',
    fields: ["id", "text"],
    defaultTreeConfig: {
        emptyText: '',
        loadingText: 'Loading...',
        loadingHeight: 70,
        minWidth: 70,
        height: 300,
        shadow: 'sides'
    },
    initComponent: function () {
        var me = this;
   var g = MyApp.Base.LocalSet.getGolbal();
        me.addEvents(
            'itemClick',
            'checkChange'
        );

        if (!me.displayTpl) {
            me.displayTpl = Ext.create('Ext.XTemplate',
                '<tpl for=".">' +
                    '{[typeof values === "string" ? values : values["' + me.displayField + '"]]}' +
                    '<tpl if="xindex < xcount">' + me.delimiter + '</tpl>' +
                '</tpl>'
            );
        } else if (Ext.isString(me.displayTpl)) {
            me.displayTpl = Ext.create('Ext.XTemplate', me.displayTpl);
        }

        me.store = Ext.create('Ext.data.TreeStore', {
            fields: me.fields,
            nodeParam: 'parentID',
            proxy: {
                type: 'ajax',
                url: me.url,
                reader: {
                    type: 'json',
                    root: me.root
                }
            },
            root: {
                id: g.company.id,
                text: g.company.name
            },
            autoLoad: true
        });
        me.callParent();
    },
    createPicker: function () {
        var me = this,
            picker,
            menuCls = Ext.baseCSSPrefix + 'menu',
            opts = Ext.apply({
                id:'carPicker',
                pickerField: me,
                floating: true,
                hidden: true,
                ownerCt: me.ownerCt,
                displayField: me.displayField,
                cls: me.el.up('.' + menuCls) ? menuCls : '',
                store: me.store,
                autoScroll: true,
                rootVisible: true,
                focusOnToFront: false
            },
            me.treeConfig,
            me.defaultTreeConfig
            );

        picker = me.picker = Ext.create('Ext.tree.Panel', opts);

        me.mon(picker, {
            itemclick: me.onItemClick,
            checkchange: me.onCheckChange,
            scope: me
        });

        return picker;
    },
    findRecordByNodeId: function (id) {
        var me = this;
        return me.store.getNodeById(id);
    },
    getDisplayValue: function () {
        return this.displayTpl.apply(this.displayTplData);
    },
    getList: function() {
        var node = null;
        var picker = Ext.getCmp('carPicker');
        if(picker) node = Ext.getCmp('carPicker').getRootNode().getChildAt(0) ;
        var values = [];
      
        function _get(nd) {
            nd.eachChild(function(cnd){
                if(!cnd.isLeaf()) {
                    _get(cnd);
                } else if(cnd.get('checked')){
                    values.push({id:cnd.get('id'),text:cnd.get('text')});
                }
            });
        }
        
        if(picker)_get(node);
        
        return values;
    },
    getValue: function () {
        // If the user has not changed the raw field value since a value was selected from the list,
        // then return the structured value from the selection. If the raw field value is different
        // than what would be displayed due to selection, return that raw value.
        //var node = me.picker.getRootNode();
        var node = null;
        var picker = Ext.getCmp('carPicker');
        if(picker) node = Ext.getCmp('carPicker').getRootNode().getChildAt(0) ;
        var values = [];
        
        function _get(nd) {
            nd.eachChild(function(cnd){
                if(!cnd.isLeaf()) {
                    _get(cnd);
                } else if(cnd.get('checked')){
                    values.push(cnd.get('id'));
                }
            });
        }
         if(node){
        if(picker)_get(node);
       }else{
       // picker = me.picker;
       // alert(picker);
      //  node = Ext.getCmp('carPicker').getRootNode();
      // values.push(node.get('id'));
       }
        
        return values;
        /*
        var me = this,
            picker = me.picker,
            rawValue = me.getRawValue(), //current value of text field
            value = me.value; //stored value from last selection or setValue() call

        if (me.getDisplayValue() !== rawValue) {
            value = rawValue;
            me.value = me.displayTplData = me.valueModels = null;
            if (picker) {
                me.ignoreSelection++;
                picker.getSelectionModel().deselectAll();
                me.ignoreSelection--;
            }
        }

        return value || "";*/
    },
    getSubmitValue: function () {
        return this.getValue();
    },
    onItemClick: function (view, record, item, index, e, eOpts) {
      
        var me = this;
        me.displayTplData = record.data;
        me.setRawValue(me.getDisplayValue());
        me.value = record.data[me.valueField];
        me.collapse();
        me.fireEvent('itemClick', me, view, record, item, index, e, eOpts);
    },
    onCheckChange:function(node, state) {
        checkNode(node);
        
        function checkNode(nd) {
            if(!nd.isLeaf()) {
                nd.eachChild(function(child){
                    if(!child.isLeaf()) checkNode(child);
                    child.set('checked', state);
                });
            }
        }
    }
});