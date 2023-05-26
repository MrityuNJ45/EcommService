// Code generated by mockery v2.28.1. DO NOT EDIT.

package mocks

import (
	context "context"

	grpc "google.golang.org/grpc"

	mock "github.com/stretchr/testify/mock"

	product "apigateway/proto/product"
)

// ProductServiceClient is an autogenerated mock type for the ProductServiceClient type
type ProductServiceClient struct {
	mock.Mock
}

// CreateProduct provides a mock function with given fields: ctx, in, opts
func (_m *ProductServiceClient) CreateProduct(ctx context.Context, in *product.CreateProductRequest, opts ...grpc.CallOption) (*product.CreateProductResponse, error) {
	_va := make([]interface{}, len(opts))
	for _i := range opts {
		_va[_i] = opts[_i]
	}
	var _ca []interface{}
	_ca = append(_ca, ctx, in)
	_ca = append(_ca, _va...)
	ret := _m.Called(_ca...)

	var r0 *product.CreateProductResponse
	var r1 error
	if rf, ok := ret.Get(0).(func(context.Context, *product.CreateProductRequest, ...grpc.CallOption) (*product.CreateProductResponse, error)); ok {
		return rf(ctx, in, opts...)
	}
	if rf, ok := ret.Get(0).(func(context.Context, *product.CreateProductRequest, ...grpc.CallOption) *product.CreateProductResponse); ok {
		r0 = rf(ctx, in, opts...)
	} else {
		if ret.Get(0) != nil {
			r0 = ret.Get(0).(*product.CreateProductResponse)
		}
	}

	if rf, ok := ret.Get(1).(func(context.Context, *product.CreateProductRequest, ...grpc.CallOption) error); ok {
		r1 = rf(ctx, in, opts...)
	} else {
		r1 = ret.Error(1)
	}

	return r0, r1
}

// GetAllProducts provides a mock function with given fields: ctx, in, opts
func (_m *ProductServiceClient) GetAllProducts(ctx context.Context, in *product.GetAllProductsRequest, opts ...grpc.CallOption) (*product.GetAllProductsResponse, error) {
	_va := make([]interface{}, len(opts))
	for _i := range opts {
		_va[_i] = opts[_i]
	}
	var _ca []interface{}
	_ca = append(_ca, ctx, in)
	_ca = append(_ca, _va...)
	ret := _m.Called(_ca...)

	var r0 *product.GetAllProductsResponse
	var r1 error
	if rf, ok := ret.Get(0).(func(context.Context, *product.GetAllProductsRequest, ...grpc.CallOption) (*product.GetAllProductsResponse, error)); ok {
		return rf(ctx, in, opts...)
	}
	if rf, ok := ret.Get(0).(func(context.Context, *product.GetAllProductsRequest, ...grpc.CallOption) *product.GetAllProductsResponse); ok {
		r0 = rf(ctx, in, opts...)
	} else {
		if ret.Get(0) != nil {
			r0 = ret.Get(0).(*product.GetAllProductsResponse)
		}
	}

	if rf, ok := ret.Get(1).(func(context.Context, *product.GetAllProductsRequest, ...grpc.CallOption) error); ok {
		r1 = rf(ctx, in, opts...)
	} else {
		r1 = ret.Error(1)
	}

	return r0, r1
}

// GetProduct provides a mock function with given fields: ctx, in, opts
func (_m *ProductServiceClient) GetProduct(ctx context.Context, in *product.GetProductRequest, opts ...grpc.CallOption) (*product.GetProductResponse, error) {
	_va := make([]interface{}, len(opts))
	for _i := range opts {
		_va[_i] = opts[_i]
	}
	var _ca []interface{}
	_ca = append(_ca, ctx, in)
	_ca = append(_ca, _va...)
	ret := _m.Called(_ca...)

	var r0 *product.GetProductResponse
	var r1 error
	if rf, ok := ret.Get(0).(func(context.Context, *product.GetProductRequest, ...grpc.CallOption) (*product.GetProductResponse, error)); ok {
		return rf(ctx, in, opts...)
	}
	if rf, ok := ret.Get(0).(func(context.Context, *product.GetProductRequest, ...grpc.CallOption) *product.GetProductResponse); ok {
		r0 = rf(ctx, in, opts...)
	} else {
		if ret.Get(0) != nil {
			r0 = ret.Get(0).(*product.GetProductResponse)
		}
	}

	if rf, ok := ret.Get(1).(func(context.Context, *product.GetProductRequest, ...grpc.CallOption) error); ok {
		r1 = rf(ctx, in, opts...)
	} else {
		r1 = ret.Error(1)
	}

	return r0, r1
}

type mockConstructorTestingTNewProductServiceClient interface {
	mock.TestingT
	Cleanup(func())
}

// NewProductServiceClient creates a new instance of ProductServiceClient. It also registers a testing interface on the mock and a cleanup function to assert the mocks expectations.
func NewProductServiceClient(t mockConstructorTestingTNewProductServiceClient) *ProductServiceClient {
	mock := &ProductServiceClient{}
	mock.Mock.Test(t)

	t.Cleanup(func() { mock.AssertExpectations(t) })

	return mock
}
